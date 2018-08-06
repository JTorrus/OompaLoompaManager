package dev.napptilus.jtorrus.oompaloompamanager.Utils

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class PaginationScrollListener(val button: FloatingActionButton, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

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
            button.hide()
        }

        if (dy < 0) {
            button.show()
        }
    }

    abstract fun isLoading(): Boolean

    abstract fun isLastPage(): Boolean

    abstract fun getTotalPageCount(): Int

    abstract fun keepLoading()
}