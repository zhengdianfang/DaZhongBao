package com.zhengdianfang.dazhongbao.models.login

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by dfgzheng on 30/07/2017.
 */
data class User(var id: String, var token: String, var realname: String, var avatar: String,
                var companyName: String, var companyAddress: String, var email: String , var position: String,
                var level: Int, var type: Int, var integrity: Int, var businessCard: String, var contactCard: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(token)
        parcel.writeString(realname)
        parcel.writeString(avatar)
        parcel.writeString(companyName)
        parcel.writeString(companyAddress)
        parcel.writeString(email)
        parcel.writeString(position)
        parcel.writeInt(level)
        parcel.writeInt(type)
        parcel.writeInt(integrity)
        parcel.writeString(businessCard)
        parcel.writeString(contactCard)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}