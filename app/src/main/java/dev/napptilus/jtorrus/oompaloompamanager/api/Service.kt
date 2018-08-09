package dev.napptilus.jtorrus.oompaloompamanager.api

import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.model.WorkerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit Service that handles the Requests the client will make
 *
 * This interface defines two methods responsible of fetching the requested data into a [WorkerResponse] or [Worker]
 *
 * @author Javier Torrus
 */
interface Service {
    @GET("napptilus/oompa-loompas")
    fun getWorkers(@Query("page") pageIndex: Int): Call<WorkerResponse>

    @GET("napptilus/oompa-loompas/{id}")
    fun getWorkerById(@Path("id") id: Int): Call<Worker>
}