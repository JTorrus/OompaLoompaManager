package dev.napptilus.jtorrus.oompaloompamanager.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import dev.napptilus.jtorrus.oompaloompamanager.R

class FilterDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val layoutInflater = activity!!.layoutInflater

        builder
                .setTitle("Filters")
                .setView(layoutInflater.inflate(R.layout.filter_dialog_fragment, null))
                .setPositiveButton("Ok") { dialog, which ->

                }
                .setNegativeButton("Cancel") { _, _ ->
                    this.dialog.cancel()
                }

        return builder.create()
    }
}