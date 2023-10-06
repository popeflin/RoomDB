package com.dewabrata.simpledatabase.roomdb

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var userId : Int,
    var nama : String,
    var email : String,
    var photo : String
):Parcelable





