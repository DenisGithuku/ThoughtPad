
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
package com.gitsoft.thoughtpad.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "checklist",
    foreignKeys =
        [
            ForeignKey(
                entity = Note::class,
                parentColumns = ["noteId"],
                childColumns = ["noteId"],
                onDelete = ForeignKey.CASCADE
            )
        ]
)
data class CheckListItem(
    @PrimaryKey(autoGenerate = true) val checkListItemId: Long = 0,
    @ColumnInfo(index = true) val noteId: Long? = null,
    val text: String? = null,
    val isChecked: Boolean = false
)
