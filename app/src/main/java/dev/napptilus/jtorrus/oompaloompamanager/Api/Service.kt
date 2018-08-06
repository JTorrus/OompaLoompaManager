package dev.napptilus.jtorrus.oompaloompamanager.Api

import android.database.Observable
import dev.napptilus.jtorrus.oompaloompamanager.Model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.Model.WorkerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("napptilus/oompa-loompas")
    fun getWorkers(@Query("page") pageIndex: Int): Call<WorkerResponse>

    @GET("napptilus/oompa-loompas/{id}")
    fun getWorkerById(@Path("id") id: Int): Call<Worker>
}