
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
package core.gitsoft.thoughtpad.core.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gitsoft.thoughtpad.core.common.AppConstants
import com.gitsoft.thoughtpad.core.data.R
import kotlin.random.Random

private val requestCode = (System.currentTimeMillis() % 10000 + Random.nextInt(0, 1000)).toInt()

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            notify(
                notificationTitle = it.extras?.getString(AppConstants.notificationTitleKey) ?: return,
                taskContent = it.extras?.getString(AppConstants.taskContentKey) ?: return,
                context = context!!,
                notificationId = AppConstants.noteReminderNotificationId,
                channelId = AppConstants.notificationChannelId
            )
        }
    }

    private fun notify(
        notificationTitle: String,
        taskContent: String,
        context: Context,
        notificationId: Int,
        channelId: String
    ) {
        // Create an intent to open the MainActivity
        val mainIntent =
            Intent("${context.packageName}.${AppConstants.actionOpenMainActivity}").apply {
                setPackage(context.packageName)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                requestCode,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(notificationTitle)
                .setContentText(taskContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_time)
                .build()

        notificationManager.notify(notificationId, notification)
    }
}
