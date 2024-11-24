
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package core.gitsoft.thoughtpad.core.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.gitsoft.thoughtpad.core.common.AppConstants
import com.gitsoft.thoughtpad.core.common.safeDbCall
import com.gitsoft.thoughtpad.core.common.safeDbReactiveDataRead
import com.gitsoft.thoughtpad.core.database.NotesDatabaseDao
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag
import core.gitsoft.thoughtpad.core.data.AlarmReceiver
import core.gitsoft.thoughtpad.core.data.CryptoManager
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializer
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.security.KeyStoreException

private const val TAG = "NotesRepositoryImpl"
private val taskReminderTitles =
    listOf(
        "Stay on Track Today!",
        "Small Steps Count – Let’s Get Started!",
        "Progress Awaits – Make Today Count!",
        "Just a Little Progress to Go!",
        "Let's Keep Momentum Going!",
        "Another Step Closer to Your Goal!",
        "Today’s a Great Day for Progress!",
        "Focus Time! Let’s Get it Done.",
        "Small Wins Lead to Big Results!",
        "Make Today’s Goals Happen!",
        "Consistency is Key – Keep Pushing!",
        "Just a Little Effort for a Big Impact!",
        "It’s Progress Time – Let’s Move!",
        "Take Action Toward Your Goals!",
        "Let’s Work Toward the Finish Line!",
        "Success is Built on Daily Progress!",
        "Remember Your Goals – Push Forward!",
        "One Step Closer Every Day!",
        "Your Goals Are Within Reach!",
        "Make the Most of Today’s Opportunities!",
        "Keep the Momentum Going Strong!",
        "You're Building Something Great!",
        "Success Starts with a Single Step!",
        "Every Effort Adds Up – Let's Go!",
        "Progress Over Perfection – Take Action!",
        "One Day Closer to Achieving It!",
        "Focus Forward – Today is Yours!",
        "Create a Productive Day!",
        "Aim for Progress, Not Perfection!",
        "Your Future Self Will Thank You!"
    )

internal class NotesRepositoryImpl(
    private val notesDatabaseDao: NotesDatabaseDao,
    private val userPrefsRepository: UserPrefsRepository,
    private val cypherManager: CryptoManager,
    private val context: Context
) : NotesRepository {
    override val allNotes: Flow<List<DataWithNotesCheckListItemsAndTags>>
        get() = notesDatabaseDao.loadAllNotes().safeDbReactiveDataRead { emptyList() }

    override suspend fun getNoteWithDataById(id: Long): DataWithNotesCheckListItemsAndTags =
        safeDbCall {
            notesDatabaseDao.noteDataById(id)
        }

    override suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ): Unit = safeDbCall {
        val oldReminderTime = notesDatabaseDao.getNoteById(note.noteId).reminderTime

        notesDatabaseDao.updateNoteWithDetails(note, checklistItems, tags)

        if (note.reminderTime == null) return@safeDbCall

        /** If the reminderTime has not changed or is in the past, return */
        val now = Calendar.getInstance().timeInMillis
        if (oldReminderTime == note.reminderTime || note.reminderTime?.let { it <= now } == true) {
            return@safeDbCall
        }

        /**
         * Schedule only if notification permissions are granted (required for API Tiramisu and above
         * Always true for other devices.
         */
        if (userPrefsRepository.userPrefs.first().isNotificationPermissionsGranted) {
            setTaskReminder(
                alarmTime = note.reminderTime ?: return@safeDbCall,
                notificationTitle = taskReminderTitles[(0..taskReminderTitles.size).random()],
                taskTitle = note.noteTitle ?: note.noteText ?: "Remember to Make Progress Today!"
            )
        }
    }

    override suspend fun insertTags(tags: List<Tag>) = safeDbCall {
        notesDatabaseDao.insertTags(tags)
    }

    override suspend fun insertTag(tag: Tag): Long = safeDbCall { notesDatabaseDao.insertTag(tag) }

    override suspend fun getTagById(tagId: Long): Tag = safeDbCall { notesDatabaseDao.getTag(tagId) }

    override val allTags: Flow<List<Tag>>
        get() = notesDatabaseDao.getTags().safeDbReactiveDataRead { emptyList() }

    override suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ): Long = safeDbCall {
        val noteId = notesDatabaseDao.insertNoteWithDetails(note, checklistItems, tags)

        if (note.reminderTime == null) return@safeDbCall noteId

        /**
         * Schedule only if notification permissions are granted (required for API Tiramisu and above
         * Always true for other devices.
         */
        if (userPrefsRepository.userPrefs.first().isNotificationPermissionsGranted) {
            setTaskReminder(
                alarmTime = note.reminderTime ?: return@safeDbCall noteId,
                notificationTitle = taskReminderTitles[(0..taskReminderTitles.size).random()],
                taskTitle = note.noteTitle ?: note.noteText ?: "Remember to Make Progress Today!"
            )
        }
        noteId
    }

    override suspend fun getNoteById(id: Long): Note = safeDbCall { notesDatabaseDao.getNoteById(id) }

    override suspend fun updateNote(note: Note): Int = safeDbCall {
        notesDatabaseDao.updateNote(note)
    }

    override suspend fun deleteNoteById(id: Long): Int = safeDbCall {
        notesDatabaseDao.deleteNoteWithDetails(id)
    }

    override suspend fun updateTag(tag: Tag): Int = safeDbCall { notesDatabaseDao.updateTag(tag) }

    override suspend fun deleteTag(tag: Tag): Int = safeDbCall {
        notesDatabaseDao.deleteTagWithNoteAssociation(tag)
    }

    override suspend fun encryptPassword(password: String): ByteArray? {
        return try {
            val outputStream = ByteArrayOutputStream()
            cypherManager.encrypt(password.toByteArray(), outputStream)
            outputStream.toByteArray()
        } catch (e: KeyStoreException) {
            Timber.tag("Encryption exception").e(e)
            null
        }
    }

    override suspend fun decryptPassword(inputStream: InputStream): ByteArray? {
        return try {
            cypherManager.decrypt(inputStream)
        } catch (e: KeyStoreException) {
            Timber.tag("Encryption exception").e(e)
            null
        }
    }


    private fun setTaskReminder(alarmTime: Long, notificationTitle: String, taskTitle: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(AppConstants.notificationTitleKey, notificationTitle)
                putExtra(AppConstants.taskContentKey, taskTitle)
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }
}
