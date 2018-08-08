package dev.napptilus.jtorrus.oompaloompamanager.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import dev.napptilus.jtorrus.oompaloompamanager.R
import dev.napptilus.jtorrus.oompaloompamanager.adapters.PaginationAdapter
import dev.napptilus.jtorrus.oompaloompamanager.api.Client
import dev.napptilus.jtorrus.oompaloompamanager.api.Service
import dev.napptilus.jtorrus.oompaloompamanager.fragments.FilterDialog
import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.model.WorkerResponse
import dev.napptilus.jtorrus.oompaloompamanager.utils.PaginationAdapterCallback
import dev.napptilus.jtorrus.oompaloompamanager.utils.PaginationScrollListener
import dev.napptilus.jtorrus.oompaloompamanager.utils.RecyclerItemDivider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity(), PaginationAdapterCallback, FilterDialog.FilterDialogListener {
    private val pageStart = 1
    private val pageEnd = 20

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = pageStart
    private var genderSelection: String = "all"
    private var professionSelection: String = "all"

    private lateinit var adapter: PaginationAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var service: Service
    private lateinit var dialog: FilterDialog
    private lateinit var results: List<Worker>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onDialogPositiveClick(dialog: DialogFragment, genderSelection: String, professionSelection: String) {
        loadFirstPage()
        this.genderSelection = genderSelection
        this.professionSelection = professionSelection
    }

    private fun filterCheck(genderSelection: String, professionSelection: String) {
        if (genderSelection.trim().toLowerCase() == "all" && professionSelection.trim().toLowerCase() != "all") {
            for (result in results) {
                if (professionSelection.trim().toLowerCase() != result.profession!!.trim().toLowerCase()) {
                    adapter.remove(result)
                }
            }
        } else if (genderSelection.trim().toLowerCase() != "all" && professionSelection.trim().toLowerCase() == "all") {
            for (result in results) {
                if (genderSelection.trim().first().toLowerCase().toString() != result.gender!!.trim().toLowerCase()) {
                    adapter.remove(result)
                }
            }
        } else if (genderSelection.trim().toLowerCase() != "all" && professionSelection.trim().toLowerCase() != "all") {
            for (result in results) {
                if ((genderSelection.trim().first().toLowerCase().toString() != result.gender!!.trim().toLowerCase()) || (professionSelection.trim().toLowerCase() != result.profession!!.trim().toLowerCase())) {
                    adapter.remove(result)
                }
            }
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = workers_recycler
        progressBar = main_progress
        adapter = PaginationAdapter(this)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        swipeRefreshLayout = swipe_refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        service = Client.getClient()!!.create(Service::class.java)

        prepareRecycler()
        loadFirstPage()
        enableRetryControls()
        setSwipeRefreshLayoutListener()
        enableFabControls()
    }

    override fun onStop() {
        super.onStop()
        callWorkers().cancel()
    }

    override fun retryPageLoad() {
        loadNextPage()
    }

    private fun prepareRecycler() {
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
    }

    private fun setSwipeRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener {
            genderSelection= "all"
            professionSelection = "all"
            loadFirstPage()
        }
    }

    private fun enableRetryControls() {
        error_btn_retry.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            loadFirstPage()
        }
    }

    private fun enableFabControls() {
        filter_button.setOnClickListener {
            dialog = FilterDialog()

            val args = Bundle()
            args.putInt("genderPos", resources.getStringArray(R.array.gender_filters).indexOf(genderSelection))
            args.putInt("profPos", resources.getStringArray(R.array.profession_filters).indexOf(professionSelection))

            dialog.arguments = args
            dialog.show(supportFragmentManager, "FilterDialog")
        }
    }

    private fun loadFirstPage() {
        if (currentPage != pageStart) {
            currentPage = pageStart
        }

        firstResponseCallback()
    }

    private fun showErrorLayout(throwable: Throwable) {
        if (error_layout.visibility == View.GONE) {
            recyclerView.visibility = View.GONE
            filter_button.visibility = View.GONE
            error_layout.visibility = View.VISIBLE
            error_txt_cause.text = detectErrorCause(throwable)
        }
    }

    private fun hideErrorLayout() {
        if (error_layout.visibility != View.GONE) {
            recyclerView.visibility = View.VISIBLE
            filter_button.visibility = View.VISIBLE
            error_layout.visibility = View.GONE
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

    private fun isNetworkOn(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }

    private fun loadNextPage() {
        nextResponseCallback()
    }

    private fun firstResponseCallback() {
        callWorkers().enqueue(object : Callback<WorkerResponse> {
            override fun onFailure(call: Call<WorkerResponse>?, t: Throwable?) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                showErrorLayout(t!!)
            }

            override fun onResponse(call: Call<WorkerResponse>?, response: Response<WorkerResponse>?) {
                adapter.clear()
                results = fetchResults(response!!)
                swipeRefreshLayout.isRefreshing = false

                progressBar.visibility = View.GONE
                hideErrorLayout()
                adapter.addAll(results)

                filterCheck(genderSelection, professionSelection)

                if (currentPage <= pageEnd) {
                    adapter.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }
        })
    }

    private fun nextResponseCallback() {
        callWorkers().enqueue(object : Callback<WorkerResponse> {
            override fun onFailure(call: Call<WorkerResponse>?, t: Throwable?) {
                adapter.showRetry(true, detectErrorCause(t!!))
            }

            override fun onResponse(call: Call<WorkerResponse>?, response: Response<WorkerResponse>?) {
                adapter.removeLoadingFooter()
                isLoading = false

                results = fetchResults(response!!)
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
