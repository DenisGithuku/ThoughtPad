
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
package com.gitsoft.thoughtpad

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.gitsoft.thoughtpad.core.common.AppConstants
import com.gitsoft.thoughtpad.core.database.databaseModule
import com.gitsoft.thoughtpad.core.datastore.prefsModule
import com.gitsoft.thoughtpad.feature.addnote.addNoteModule
import com.gitsoft.thoughtpad.feature.notelist.noteListModule
import com.gitsoft.thoughtpad.feature.settings.settingsModule
import com.gitsoft.thoughtpad.feature.tags.tagModule
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.sessions.BuildConfig
import core.gitsoft.thoughtpad.core.data.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class ThoughtPadApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ThoughtPadApplication)
            modules(
                appModule,
                dataModule,
                databaseModule,
                prefsModule,
                settingsModule,
                noteListModule,
                addNoteModule,
                tagModule
            )
        }

        initTimber()

        createNotificationChannel()
    }

    private fun initTimber() {
        when {
            BuildConfig.DEBUG -> {
                Timber.plant(
                    object : Timber.DebugTree() {
                        override fun createStackElementTag(element: StackTraceElement): String {
                            return super.createStackElementTag(element) + ":" + element.lineNumber
                        }
                    }
                )
            }
            else -> {
                Timber.plant(CrashlyticsTree())
            }
        }
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                    AppConstants.notificationChannelId,
                    AppConstants.notificationChannelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                .apply {
                    setShowBadge(true)
                    enableVibration(true)
                    description = AppConstants.notificationChannelDescription
                }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

class CrashlyticsTree : Timber.Tree() {

    private val crashlytics: FirebaseCrashlytics by lazy { FirebaseCrashlytics.getInstance() }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        if (BuildConfig.DEBUG) {
            crashlytics.isCrashlyticsCollectionEnabled = false
            return
        }

        crashlytics.setCustomKey(CrashlyticsKeys.CRASHLYTICS_KEY_PRIORITY, priority)
        if (tag != null) {
            crashlytics.setCustomKey(CrashlyticsKeys.CRASHLYTICS_KEY_TAG, tag)
        }
        crashlytics.setCustomKey(CrashlyticsKeys.CRASHLYTICS_KEY_MESSAGE, message)

        if (t == null) {
            crashlytics.recordException(Throwable(message))
        } else {
            crashlytics.recordException(t)
        }
    }
}

object CrashlyticsKeys {
    const val CRASHLYTICS_KEY_PRIORITY = "priority"
    const val CRASHLYTICS_KEY_TAG = "tag"
    const val CRASHLYTICS_KEY_MESSAGE = "message"
}
