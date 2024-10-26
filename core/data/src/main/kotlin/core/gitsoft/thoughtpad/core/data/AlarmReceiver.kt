
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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gitsoft.thoughtpad.core.common.AppConstants
import com.gitsoft.thoughtpad.core.data.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            notify(
                message = it.extras?.getString("Reminder") ?: return,
                context = context!!,
                notificationId = AppConstants.noteReminderNotificationId,
                channelId = AppConstants.notificationChannelId
            )
        }
    }

    private fun notify(message: String, context: Context, notificationId: Int, channelId: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle("Remember to Make Progress Today!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_time)
                .build()

        notificationManager.notify(notificationId, notification)
    }
}
