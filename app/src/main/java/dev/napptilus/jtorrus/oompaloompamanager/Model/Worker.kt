package dev.napptilus.jtorrus.oompaloompamanager.Model

import com.google.gson.annotations.SerializedName

class Worker() {
    @SerializedName("first_name")
    var firstName: String = ""

    @SerializedName("last_name")
    var lastName: String = ""

    @SerializedName("favorite")
    var favorite: ArrayList<String> = ArrayList()

    @SerializedName("gender")
    var gender: String = ""

    @SerializedName("image")
    var image: String = ""

    @SerializedName("profession")
    var profession: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("age")
    var age: Int = 0

    @SerializedName("country")
    var country: String = ""

    @SerializedName("height")
    var height: Int = 0

    @SerializedName("id")
    var id: Int = 0

    constructor(firstName: String, lastName: String, favorite: ArrayList<String>, gender: String, image: String, profession: String, email: String, age: Int, country: String, height: Int, id: Int): this() {
        this.firstName = firstName
        this.lastName = lastName
        this.favorite = favorite
        this.gender = gender
        this.image = image
        this.profession = profession
        this.email = email
        this.age = age
        this.country = country
        this.height = height
        this.id = id
    }
}