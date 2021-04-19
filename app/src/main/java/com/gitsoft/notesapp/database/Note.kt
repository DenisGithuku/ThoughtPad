package com.gitsoft.notesapp.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
        @PrimaryKey(autoGenerate = true)
        val noteId: Int,
        @ColumnInfo(name = "title")
        val noteTitle: String?,
        @ColumnInfo(name = "text")
        val noteText: String?
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(noteId)
                parcel.writeString(noteTitle)
                parcel.writeString(noteText)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Note> {
                override fun createFromParcel(parcel: Parcel): Note {
                        return Note(parcel)
                }

                override fun newArray(size: Int): Array<Note?> {
                        return arrayOfNulls(size)
                }
        }
}
