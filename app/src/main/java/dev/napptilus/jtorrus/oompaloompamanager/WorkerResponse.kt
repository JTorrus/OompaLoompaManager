package dev.napptilus.jtorrus.oompaloompamanager

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class WorkerResponse() : Parcelable {
    @SerializedName("current")
    var current: Int = 0

    @SerializedName("total")
    var total: Int = 20

    @SerializedName("results")
    var results: List<Worker> = ArrayList()

    constructor(parcel: Parcel) : this() {
        current = parcel.readInt()
        total = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(current)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkerResponse> {
        override fun createFromParcel(parcel: Parcel): WorkerResponse {
            return WorkerResponse(parcel)
        }

        override fun newArray(size: Int): Array<WorkerResponse?> {
            return arrayOfNulls(size)
        }
    }
}