package dev.napptilus.jtorrus.oompaloompamanager.Adapters

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import dev.napptilus.jtorrus.oompaloompamanager.Model.Worker

class WorkersAdapter(val workers: ArrayList<Worker>) : RecyclerView.Adapter<WorkersAdapter.Companion.WorkersViewHolder>() {

    companion object {
        class WorkersViewHolder(val mConstraint: ConstraintLayout) : RecyclerView.ViewHolder(mConstraint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersViewHolder {
        TODO()
    }

    override fun getItemCount(): Int {
        TODO()
    }

    override fun onBindViewHolder(holder: WorkersViewHolder, position: Int) {
        TODO()
    }
}