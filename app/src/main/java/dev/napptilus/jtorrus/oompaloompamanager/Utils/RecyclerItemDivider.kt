package dev.napptilus.jtorrus.oompaloompamanager.Utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import dev.napptilus.jtorrus.oompaloompamanager.R

class RecyclerItemDivider(context: Context): RecyclerView.ItemDecoration() {
    var mDivider: Drawable = ContextCompat.getDrawable(context, R.drawable.line_divider)!!

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        parent?.let {
            val left = it.paddingLeft + 32
            val right = it.width - it.paddingRight - 32
            val childCount = it.childCount

            for (i in 0 until childCount) {
                val child = it.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider.intrinsicHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }
}