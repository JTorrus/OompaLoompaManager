package dev.napptilus.jtorrus.oompaloompamanager.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WorkerResponse(
        @SerializedName("current")
        @Expose
        var current: Int? = null,

        @SerializedName("total")
        @Expose
        var total: Int? = null,

        @SerializedName("results")
        @Expose
        var results: List<Worker>? = null
)