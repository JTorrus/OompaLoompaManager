package dev.napptilus.jtorrus.oompaloompamanager.Model

import com.google.gson.annotations.SerializedName

class WorkerResponse() {
    @SerializedName("current")
    var current: Int = 0

    @SerializedName("total")
    var total: Int = 20

    @SerializedName("results")
    var results: List<Worker> = ArrayList()
}