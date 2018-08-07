package dev.napptilus.jtorrus.oompaloompamanager.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import dev.napptilus.jtorrus.oompaloompamanager.adapters.PaginationAdapter
import dev.napptilus.jtorrus.oompaloompamanager.api.Client
import dev.napptilus.jtorrus.oompaloompamanager.api.Service
import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.model.WorkerResponse
import dev.napptilus.jtorrus.oompaloompamanager.R
import dev.napptilus.jtorrus.oompaloompamanager.utils.PaginationScrollListener
import dev.napptilus.jtorrus.oompaloompamanager.utils.RecyclerItemDivider
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val pageStart = 1
    private val pageEnd = 20

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = pageStart

    private lateinit var adapter: PaginationAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var service: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = workers_recycler
        progressBar = main_progress

        adapter = PaginationAdapter(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(RecyclerItemDivider(applicationContext))
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : PaginationScrollListener(filter_button, linearLayoutManager) {
            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun getTotalPageCount(): Int {
                return pageEnd
            }

            override fun keepLoading() {
                isLoading = true
                currentPage += 1
                loadNextPage()
            }

        })

        service = Client.getClient()!!.create(Service::class.java)

        loadFirstPage()
    }

    override fun onStop() {
        super.onStop()
        callWorkers().cancel()
    }

    private fun loadFirstPage() {
        callWorkers().enqueue(object : Callback<WorkerResponse> {
            override fun onFailure(call: Call<WorkerResponse>?, t: Throwable?) {
                Log.e("Error", t!!.message)
            }

            override fun onResponse(call: Call<WorkerResponse>?, response: Response<WorkerResponse>?) {
                val results: List<Worker> = fetchResults(response!!)

                progressBar.visibility = View.GONE
                adapter.addAll(results)

                if (currentPage <= pageEnd) {
                    adapter.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }
        })
    }

    private fun loadNextPage() {
        callWorkers().enqueue(object : Callback<WorkerResponse> {
            override fun onFailure(call: Call<WorkerResponse>?, t: Throwable?) {
                Log.e("Error", t!!.localizedMessage)
            }

            override fun onResponse(call: Call<WorkerResponse>?, response: Response<WorkerResponse>?) {
                adapter.removeLoadingFooter()
                isLoading = false

                val results: List<Worker> = fetchResults(response!!)
                adapter.addAll(results)

                if (currentPage != pageEnd) {
                    adapter.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }

        })
    }

    private fun callWorkers(): Call<WorkerResponse> {
        return service.getWorkers(currentPage)
    }

    private fun fetchResults(response: Response<WorkerResponse>): List<Worker> {
        val workers = response.body()
        return workers!!.results!!
    }
}
