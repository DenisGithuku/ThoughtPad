
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
package com.gitsoft.thoughtpad.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gitsoft.thoughtpad.core.datastore.migrations.Migration_0_1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

private const val USER_PREFERENCES = "user_preferences"

val prefsModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create( // deserialize with a fallback strategy
            corruptionHandler =
                ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ), // list of migrations on how to move from the previous datastore
            migrations =
                listOf(SharedPreferencesMigration(get<Context>(), USER_PREFERENCES), Migration_0_1),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { get<Context>().preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    single<UserPrefsDataSource> { UserPrefsDataSource(get()) }
}
