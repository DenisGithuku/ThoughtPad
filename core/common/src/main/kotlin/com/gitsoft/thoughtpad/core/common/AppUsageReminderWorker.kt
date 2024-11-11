
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
package com.gitsoft.thoughtpad.core.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gitsoft.thoughtpad.core.common.WorkConstants.notificationMessages
import java.io.IOException
import kotlin.apply
import timber.log.Timber

object WorkConstants {
    const val WORK_NAME = "AppUsageReminderWorker"
    const val CHANNEL_ID = "periodic_reminder_notification_channel"
    const val CHANNEL_NAME = "App Usage Reminders"
    val NOTIFICATION_ID = System.currentTimeMillis().toInt()

    val notificationMessages =
        mapOf(
            "You Got This!" to "Don't forget, you're capable of amazing things. Keep pushing forward!",
            "Small Wins Matter" to "Celebrate your small victories today. They lead to big achievements!",
            "Stay Focused" to "A little progress each day adds up to big results. Stay on track!",
            "Embrace the Challenge" to
                "Challenges are opportunities in disguise. Face them with confidence!",
            "Believe in Yourself" to "You have what it takes to achieve your goals. Believe in yourself!",
            "Hydrate!" to "Drink a glass of water now. Your body will thank you for it!",
            "Stretch It Out" to "Take a quick break to stretch. Your muscles need it!",
            "Breathe Deeply" to "Take a moment to breathe deeply and reset. Inhale... Exhale...",
            "Posture Check" to "Sit up straight and adjust your posture. Your back will appreciate it!",
            "Snack Time" to "Grab a healthy snack to refuel your energy. How about some fruits or nuts?",
            "Clear the Clutter" to
                "Take 5 minutes to tidy up your workspace. A clear space = a clear mind.",
            "Focus on One Task" to
                "Multitasking can be overwhelming. Focus on completing one thing at a time.",
            "Time to Prioritize" to "Review your to-do list. What's the most important task right now?",
            "Take a Break" to "You've been working hard. Take a 5-minute break to recharge.",
            "Review Your Goals" to "Are you on track with your goals? Take a moment to reassess.",
            "Check In with Yourself" to "How are you feeling right now? It's okay to pause and reflect.",
            "Gratitude Moment" to "Take a moment to think of one thing you're grateful for today.",
            "Mindful Moment" to "Close your eyes for a minute and focus on your breath. Be present.",
            "Digital Detox" to "Put down your phone for a bit. Enjoy a moment of screen-free time.",
            "Smile!" to "A simple smile can boost your mood instantly. Give it a try!",
            "Wise Words" to
                "Success is not final, failure is not fatal: it is the courage to continue that counts. – Winston Churchill",
            "Keep Going" to "The only way to do great work is to love what you do. – Steve Jobs",
            "Don't Stop Now" to
                "The best time to plant a tree was 20 years ago. The second-best time is now. – Chinese Proverb",
            "Stay Positive" to "Your attitude determines your direction. Keep it positive!",
            "You Are Enough" to "You are exactly where you need to be. Keep trusting the journey."
        )
}

class AppUsageReminderWorker(context: Context, workParams: WorkerParameters) :
    Worker(context, workParams) {
    override fun doWork(): Result {
        return handleErroneousWorkOperation {
            showReminder()
            Result.success()
        }
    }

    private fun showReminder() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val soundUri = Uri.parse("android.resource://${applicationContext.packageName}/raw/lowport")

        val audioAttributes =
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

        val channel =
            NotificationChannel(
                    WorkConstants.CHANNEL_ID,
                    WorkConstants.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                .apply { setSound(soundUri, audioAttributes) }
        notificationManager.createNotificationChannel(channel)

        // Get a random message everytime
        val notificationMessage =
            notificationMessages.entries.elementAt((0..notificationMessages.size).random())

        val notification =
            NotificationCompat.Builder(applicationContext, WorkConstants.CHANNEL_ID)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notificationMessage.key)
                .setContentText(notificationMessage.value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        notificationManager.notify(WorkConstants.NOTIFICATION_ID, notification)

        // Log the notification using Timber
        Timber.i(
            "Notification posted - Title: ${notificationMessage.key}, Message: ${notificationMessage.value}"
        )
    }
}

inline fun handleErroneousWorkOperation(
    operation: () -> ListenableWorker.Result
): ListenableWorker.Result {
    return try {
        operation()
    } catch (e: IllegalStateException) {
        Timber.e(e, "IllegalStateException: Unable to show reminder due to invalid state.")
        ListenableWorker.Result.failure()
    } catch (e: NullPointerException) {
        Timber.e(e, "NullPointerException: Encountered a null reference while showing reminder.")
        ListenableWorker.Result.failure()
    } catch (e: SecurityException) {
        Timber.e(e, "SecurityException: Missing required permissions to show reminder.")
        ListenableWorker.Result.failure()
    } catch (e: IOException) {
        Timber.e(e, "IOException: I/O error occurred while showing reminder.")
        ListenableWorker.Result.retry()
        ListenableWorker.Result.failure()
    } catch (e: Exception) {
        Timber.e(e, "Unexpected error: ${e.message}")
        ListenableWorker.Result.failure()
    }
}
