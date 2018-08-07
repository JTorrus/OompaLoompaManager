package dev.napptilus.jtorrus.oompaloompamanager.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import dev.napptilus.jtorrus.oompaloompamanager.R
import dev.napptilus.jtorrus.oompaloompamanager.api.Client
import dev.napptilus.jtorrus.oompaloompamanager.api.Service
import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.details_lower_section.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    private lateinit var service: Service
    private var retrievedId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar!!.title = "Details"

        service = Client.getClient()!!.create(Service::class.java)

        retrievedId = getBundledData()

        prepareLayout()
    }

    private fun callWorkerById(): Call<Worker> {
        return service.getWorkerById(retrievedId!!)
    }

    private fun fetchResults(response: Response<Worker>): Worker {
        val worker = response.body()
        return worker!!
    }

    private fun prepareLayout() {
        callWorkerById().enqueue(object : Callback<Worker> {
            override fun onFailure(call: Call<Worker>?, t: Throwable?) {
                Log.e("Error", t!!.message)
            }

            override fun onResponse(call: Call<Worker>?, response: Response<Worker>?) {
                val result = fetchResults(response!!)

                Glide
                        .with(applicationContext)
                        .load(result.image)
                        .into(details_pic)

                details_name.text = applicationContext.getString(R.string.full_name, "${result.firstName}", "${result.lastName}")
                details_email.text = result.email
                details_location.text = result.country
                details_profession.text = result.profession
                details_gender.text = result.gender
                details_age.text = result.age.toString()
                details_height.text = result.height.toString()
                details_color.text = result.favorite!!["color"]
                details_food.text = result.favorite!!["food"]
            }
        })
    }

    private fun getBundledData(): Int {
        return intent.extras["OompaId"] as Int
    }
}
