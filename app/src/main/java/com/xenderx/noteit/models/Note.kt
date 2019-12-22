package com.xenderx.noteit.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "content")var content: String,
    @ColumnInfo(name = "timestamp")var timestamp: String
) : Parcelable {
    @Ignore
    constructor(title: String, content: String, timestamp: String) :
            this(0, title, content, timestamp)

    @Ignore
    constructor() : this(0, "", "", "")
}