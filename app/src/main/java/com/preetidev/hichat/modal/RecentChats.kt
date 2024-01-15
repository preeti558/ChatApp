package com.preetidev.hichat.modal

import android.os.Parcel
import android.os.Parcelable

data class RecentChats(
    val userid: String? = "",
    val status : String? = "",
    val imageUrl : String? = "",
    val username: String? = "",
    val useremail : String? ="",

                       ) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userid)
        parcel.writeString(status)
        parcel.writeString(imageUrl)
        parcel.writeString(username)
        parcel.writeString(useremail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecentChats> {
        override fun createFromParcel(parcel: Parcel): RecentChats {
            return RecentChats(parcel)
        }

        override fun newArray(size: Int): Array<RecentChats?> {
            return arrayOfNulls(size)
        }
    }


}