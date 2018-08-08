package dev.napptilus.jtorrus.oompaloompamanager.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import dev.napptilus.jtorrus.oompaloompamanager.R
import dev.napptilus.jtorrus.oompaloompamanager.api.Client
import dev.napptilus.jtorrus.oompaloompamanager.api.Service
import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.details_lower_section.*
import kotlinx.android.synthetic.main.error_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class DetailsActivity : AppCompatActivity() {
    private var retrievedId: Int? = null

    private lateinit var service: Service
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar!!.title = getString(R.string.title_det_toolbar)

        progressBar = details_progress
        service = Client.getClient()!!.create(Service::class.java)
        retrievedId = getBundledData()

        prepareLayout()
        enableRetryControls()
    }

    override fun onStop() {
        super.onStop()
        callWorkerById().cancel()
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
                progressBar.visibility = View.GONE
                showErrorLayout(t!!)
            }

            override fun onResponse(call: Call<Worker>?, response: Response<Worker>?) {
                progressBar.visibility = View.GONE
                hideErrorLayout()
                populateFields(response)
            }
        })
    }

    private fun enableRetryControls() {
        error_btn_retry.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            prepareLayout()
        }
    }

    private fun showErrorLayout(throwable: Throwable) {
        if (error_layout.visibility == View.GONE) {
            appbar_layout.visibility = View.GONE
            lower_section_layout.visibility = View.GONE
            error_layout.visibility = View.VISIBLE
            error_txt_cause.text = detectErrorCause(throwable)
        }
    }

    private fun detectErrorCause(throwable: Throwable): String {
        return if (!isNetworkOn()) {
            getString(R.string.err_internet)
        } else if (throwable is TimeoutException) {
            getString(R.string.err_response)
        } else {
            getString(R.string.err_unkn)
        }
    }

    private fun hideErrorLayout() {
        if (error_layout.visibility != View.GONE) {
            appbar_layout.visibility = View.VISIBLE
            lower_section_layout.visibility = View.VISIBLE
            error_layout.visibility = View.GONE
        }
    }

    private fun isNetworkOn(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }

    private fun populateFields(response: Response<Worker>?) {
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

    private fun getBundledData(): Int {
        return intent.extras[getString(R.string.id_bundle_oompa)] as Int
    }
}
