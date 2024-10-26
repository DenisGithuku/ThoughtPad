
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
import com.gitsoft.thoughtpad.core.common.safeDbCall
import com.gitsoft.thoughtpad.core.common.safeDbReactiveDataRead
import com.gitsoft.thoughtpad.core.database.NotesDatabaseDao
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag
import core.gitsoft.thoughtpad.core.data.AlarmReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class NotesRepositoryImpl(
    private val notesDatabaseDao: NotesDatabaseDao,
    private val userPrefsRepository: UserPrefsRepository,
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
        notesDatabaseDao.updateNoteWithDetails(note, checklistItems, tags)
        if (
            note.reminderTime != null &&
                userPrefsRepository.userPrefs.first().isNotificationPermissionsGranted
        ) {
            setTaskReminder(
                alarmTime = note.reminderTime ?: return@safeDbCall,
                taskTitle = note.noteTitle ?: "Remember to Make Progress Today!"
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
        if (
            note.reminderTime != null &&
                userPrefsRepository.userPrefs.first().isNotificationPermissionsGranted
        ) {
            setTaskReminder(
                alarmTime = note.reminderTime ?: return@safeDbCall noteId,
                taskTitle = note.noteTitle ?: "Remember to Make Progress Today!"
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

    private fun setTaskReminder(alarmTime: Long, taskTitle: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent =
            Intent(context, AlarmReceiver::class.java).apply { putExtra("Reminder", taskTitle) }

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }
}
