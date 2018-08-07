package dev.napptilus.jtorrus.oompaloompamanager.api

import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.model.WorkerResponse
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