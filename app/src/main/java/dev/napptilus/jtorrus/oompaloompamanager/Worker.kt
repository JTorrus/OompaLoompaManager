package dev.napptilus.jtorrus.oompaloompamanager

import android.net.Uri

data class Worker(val firstName: String, val lastName: String, val favorites: ArrayList<String>, val gender: String, val imageUri: Uri, val profession: String, val email: String, val age: Int, val country: String, val height: Double, val id: Int)