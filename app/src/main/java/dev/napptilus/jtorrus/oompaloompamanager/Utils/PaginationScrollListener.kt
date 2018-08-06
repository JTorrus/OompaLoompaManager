package dev.napptilus.jtorrus.oompaloompamanager.Utils

import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class PaginationScrollListener(val button: AppCompatButton, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLastPage() && !isLoading()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                keepLoading()
            }
        }

        if (dy > 0 && button.isShown) {
            button.visibility = View.GONE
        }

        if (dy < 0) {
            button.visibility = View.VISIBLE
        }
    }

    abstract fun isLoading(): Boolean

    abstract fun isLastPage(): Boolean

    abstract fun getTotalPageCount(): Int

    abstract fun keepLoading()
}