package dev.napptilus.jtorrus.oompaloompamanager.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import de.hdodenhof.circleimageview.CircleImageView
import dev.napptilus.jtorrus.oompaloompamanager.Activities.DetailsActivity
import dev.napptilus.jtorrus.oompaloompamanager.Model.Worker
import dev.napptilus.jtorrus.oompaloompamanager.R
import kotlinx.android.synthetic.main.list_item.view.*


class PaginationAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var workerResults: ArrayList<Worker> = ArrayList()
    private var isLoadingAdded = false

    companion object {
        private val ITEM = 0
        private val LOADING = 1

        class WorkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var thumbnail: CircleImageView = itemView.item_thumbnail
            var name: TextView = itemView.item_name
            var country: TextView = itemView.item_country
            var age: TextView = itemView.item_profession
        }

        class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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
            workerViewHolder.country.text = result.country

            Glide
                    .with(context)
                    .load(result.image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .into(workerViewHolder.thumbnail)

            workerViewHolder.itemView.setOnClickListener {
                val intent = Intent(workerViewHolder.itemView.context, DetailsActivity::class.java)
                val bundle = Bundle()

                bundle.putInt("OompaId", workerResults[position].id!!)
                intent.putExtras(bundle)

                workerViewHolder.itemView.context.startActivity(intent)
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
}