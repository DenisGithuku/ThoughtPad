package com.gitsoft.thoughtpad.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

const val WORK_NAME = "UpdateWidgetWorker"

class UpdateWidgetWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    // get access to repository
    private val notesRepository: NotesRepository by inject()

    override suspend fun doWork(): Result = supervisorScope {
        withContext(Dispatchers.IO) {
            try {
                // fetch tasks
                notesRepository.allNotes.collectLatest { tasks ->            // update widget
                    val glanceManager = GlanceAppWidgetManager(applicationContext)
                    val widgetIds = glanceManager.getGlanceIds(TasksWidget::class.java)
                    val widgetData = WidgetData(
                        tasks.firstOrNull { it.note.reminderTime != null }?.note?.noteTitle,
                        tasks.firstOrNull { it.checkListItems.isNotEmpty() }?.checkListItems ?: emptyList()
                    )
                    widgetIds.forEach {
                        TasksWidget().update(
                            applicationContext, it, widgetData
                        )
                    }
                }
                Result.success()
            } catch (e: CancellationException) {
                Timber.tag("UpdateWidgetWorker").e(e)
                Result.failure()
            } catch (e: Exception) {
                Timber.tag("General exception").e(e)
                Result.failure()
            }
        }
    }
}