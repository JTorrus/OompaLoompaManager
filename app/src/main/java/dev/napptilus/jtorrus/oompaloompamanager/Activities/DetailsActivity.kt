package dev.napptilus.jtorrus.oompaloompamanager.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dev.napptilus.jtorrus.oompaloompamanager.Api.Client
import dev.napptilus.jtorrus.oompaloompamanager.Api.Service
import dev.napptilus.jtorrus.oompaloompamanager.Model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.R
import retrofit2.Call
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    private lateinit var service: Service
    var retrievedId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        service = Client.getClient()!!.create(Service::class.java)

        retrievedId = getBundledId()

        callWorkerById()
    }

    private fun callWorkerById(): Call<Worker> {
        return service.getWorkerById(retrievedId!!)
    }

    private fun fetchResults(response: Response<Worker>): Worker {
        val worker = response.body()
        return worker!!
    }

    private fun prepareLayout() {

    }

    private fun getBundledId(): Int {
        return intent.extras["OompaId"] as Int
    }
}