package com.gitsoft.thoughtpad.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val WORK_NAME = "UpdateWidgetWorker"

class UpdateWidgetWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    // get access to repository
    private val notesRepository: NotesRepository by inject()

    override suspend fun doWork(): Result {
        try {
            // fetch tasks
            notesRepository.allNotes.collectLatest { tasks ->
                // update widget
                val glanceManager = GlanceAppWidgetManager(applicationContext)
                val widgetIds = glanceManager.getGlanceIds(TasksWidget::class.java)
                widgetIds.forEach {
                    TasksWidget().update(
                        applicationContext,
                        it,
                        tasks
                    )
                }

            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }
}