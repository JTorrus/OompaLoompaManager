package dev.napptilus.jtorrus.oompaloompamanager.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import dev.napptilus.jtorrus.oompaloompamanager.R
import dev.napptilus.jtorrus.oompaloompamanager.activities.DetailsActivity
import dev.napptilus.jtorrus.oompaloompamanager.model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.utils.PaginationAdapterCallback
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item_progress.view.*


class PaginationAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var workerResults: ArrayList<Worker> = ArrayList()
    private var isLoadingAdded = false
    private var retryPageLoad = false
    private var errorMessage: String? = null
    private var paginationCallback: PaginationAdapterCallback = context as PaginationAdapterCallback

    companion object {
        private val ITEM = 0
        private val LOADING = 1

        class WorkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var thumbnail: CircleImageView = itemView.item_thumbnail
            var name: TextView = itemView.item_name
            var age: TextView = itemView.item_profession
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val view = inflater.inflate(R.layout.list_item_progress, parent, false)
                viewHolder = LoadingViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return workerResults.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == workerResults.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result: Worker = workerResults[position]

        if (getItemViewType(position) == ITEM) {
            val workerViewHolder = holder as WorkerViewHolder
            workerViewHolder.name.text = context.getString(R.string.full_name, "${result.firstName}", "${result.lastName}")
            workerViewHolder.age.text = result.profession.toString()

            Glide
                    .with(context)
                    .load(result.image)
                    .into(workerViewHolder.thumbnail)

            workerViewHolder.itemView.setOnClickListener {
                val intent = Intent(workerViewHolder.itemView.context, DetailsActivity::class.java)
                val bundle = Bundle()

                bundle.putInt(context.getString(R.string.id_bundle_oompa), workerResults[position].id!!)
                intent.putExtras(bundle)

                workerViewHolder.itemView.context.startActivity(intent)
            }
        } else {
            val loadingViewHolder = holder as LoadingViewHolder

            if (retryPageLoad) {
                loadingViewHolder.errorLayout.visibility = View.VISIBLE
                loadingViewHolder.itemView.loading_progress.visibility = View.GONE
                loadingViewHolder.errorText.text = if (errorMessage != null) {
                    errorMessage
                } else {
                    context.getString(R.string.err_unkn)
                }
            } else {
                loadingViewHolder.errorLayout.visibility = View.GONE
                loadingViewHolder.itemView.loading_progress.visibility = View.VISIBLE
            }
        }
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return WorkerViewHolder(view)
    }

    fun add(worker: Worker) {
        workerResults.add(worker)
        notifyItemInserted(workerResults.size - 1)
    }

    fun addAll(moveResults: List<Worker>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Worker())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = workerResults.size - 1
        val result = getItem(position)

        workerResults.remove(result)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Worker {
        return workerResults[position]
    }

    fun showRetry(isShown: Boolean, message: String?) {
        retryPageLoad = isShown
        notifyItemChanged(workerResults.size - 1)

        message?.let {
            errorMessage = it
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            showRetry(false, errorText.text.toString())
            paginationCallback.retryPageLoad()
        }

        var retryButton: ImageButton = itemView.loadmore_retry
        var errorText: TextView = itemView.loadmore_errortxt
        var errorLayout: ConstraintLayout = itemView.loading_error

        init {
            retryButton.setOnClickListener(this)
            errorLayout.setOnClickListener(this)
        }
    }
}