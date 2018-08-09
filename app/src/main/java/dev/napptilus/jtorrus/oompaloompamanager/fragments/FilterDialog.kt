package dev.napptilus.jtorrus.oompaloompamanager.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import dev.napptilus.jtorrus.oompaloompamanager.R

/**
 * Fragment that displays a Dialog for filtering purposes
 *
 * This class handles all user's filter operations through a DialogFragment extended class, it also has a Listener to define App's behavior when one of the control buttons is pressed
 *
 * @author Javier Torrus
 */
class FilterDialog : DialogFragment() {
    interface FilterDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, genderSelection: String, professionSelection: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    lateinit var listener: FilterDialogListener
    lateinit var inflater: LayoutInflater
    lateinit var dialogLayout: View
    lateinit var genderSelection: String
    lateinit var professionSelection: String
    var genderPosition: Int = 0
    var professionPosition: Int = 0

    /**
     * In our OnCreateDialog method we instantiate the [LayoutInflater] and prepare the inflated custom view
     *
     * We create the DialogFragment with a Builder
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        inflater = activity!!.layoutInflater
        dialogLayout = inflater.inflate(R.layout.filter_dialog_fragment, null)

        builder
                .setTitle(getString(R.string.title_fil_dialog))
                .setView(dialogLayout)
                .setPositiveButton(getString(R.string.positive_button)) { dialog, _ ->
                    listener.onDialogPositiveClick(this, genderSelection, professionSelection)
                }
                .setNegativeButton(getString(R.string.negative_button)) { dialog, _ ->
                    listener.onDialogNegativeClick(this)
                }

        setSpinnerListeners()
        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listener = context as FilterDialogListener
    }

    /**
     * We get activity's arguments (last selected spinner's option) to define which option has to be selected by default in both of the spinners when DialogFragment pops up
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val genderSpinner = dialogLayout.findViewById<AppCompatSpinner>(R.id.gender_spinner)
        val professionSpinner = dialogLayout.findViewById<AppCompatSpinner>(R.id.profession_spinner)

        genderPosition = arguments!!.getInt("genderPos")
        professionPosition = arguments!!.getInt("profPos")

        genderSpinner.setSelection(genderPosition)
        professionSpinner.setSelection(professionPosition)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * We set in both of the spinners a Listener for getting notified when one of their options is changed
     */
    private fun setSpinnerListeners() {
        val genderSpinner = dialogLayout.findViewById<AppCompatSpinner>(R.id.gender_spinner)
        val professionSpinner = dialogLayout.findViewById<AppCompatSpinner>(R.id.profession_spinner)

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genderSelection = resources.getStringArray(R.array.gender_filters)[position]
            }
        }

        professionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                professionSelection = resources.getStringArray(R.array.profession_filters)[position]
            }
        }
    }
}