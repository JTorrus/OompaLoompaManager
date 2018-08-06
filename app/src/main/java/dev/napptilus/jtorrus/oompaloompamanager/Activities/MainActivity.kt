package dev.napptilus.jtorrus.oompaloompamanager.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import dev.napptilus.jtorrus.oompaloompamanager.Adapters.PaginationAdapter
import dev.napptilus.jtorrus.oompaloompamanager.Api.Client
import dev.napptilus.jtorrus.oompaloompamanager.Api.Service
import dev.napptilus.jtorrus.oompaloompamanager.Model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.Model.WorkerResponse
import dev.napptilus.jtorrus.oompaloompamanager.R
import dev.napptilus.jtorrus.oompaloompamanager.Utils.PaginationScrollListener
import dev.napptilus.jtorrus.oompaloompamanager.Utils.RecyclerItemDivider
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val PAGE_START = 1
    private val PAGE_END = 20
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START

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

        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun getTotalPageCount(): Int {
                return PAGE_END
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

    private fun loadFirstPage() {
        callWorkers().enqueue(object : Callback<WorkerResponse> {
            override fun onFailure(call: Call<WorkerResponse>?, t: Throwable?) {
                Log.e("Error", t!!.message)
            }

            override fun onResponse(call: Call<WorkerResponse>?, response: Response<WorkerResponse>?) {
                val results: List<Worker> = fetchResults(response!!)

                progressBar.visibility = View.GONE
                adapter.addAll(results)

                if (currentPage <= PAGE_END) {
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

                if (currentPage != PAGE_END) {
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
