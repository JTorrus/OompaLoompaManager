package dev.napptilus.jtorrus.oompaloompamanager.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dev.napptilus.jtorrus.oompaloompamanager.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val bundle = intent.extras
        val id = bundle.get("OompaId")

        Toast.makeText(this, id.toString(), Toast.LENGTH_LONG).show()
    }
}
