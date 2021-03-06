package dev.napptilus.jtorrus.oompaloompamanager.utils

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Listener that checks when user scrolls the RecyclerView
 */
abstract class PaginationScrollListener(val button: FloatingActionButton, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    /**
     * In the OnScrolled method we check when the app should load more items and hide/show the [FloatingActionButton]
     */
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