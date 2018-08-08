package dev.napptilus.jtorrus.oompaloompamanager.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import dev.napptilus.jtorrus.oompaloompamanager.R

class FilterDialog : DialogFragment() {
    interface FilterDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    lateinit var listener: FilterDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val layoutInflater = activity!!.layoutInflater

        builder
                .setTitle(getString(R.string.title_fil_dialog))
                .setView(layoutInflater.inflate(R.layout.filter_dialog_fragment, null))
                .setPositiveButton(getString(R.string.positive_button)) { dialog, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton(getString(R.string.negative_button)) { dialog, _ ->
                    listener.onDialogNegativeClick(this)
                }

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listener = context as FilterDialogListener
    }
}