package dev.napptilus.jtorrus.oompaloompamanager.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Worker(
        @SerializedName("first_name")
        @Expose
        var firstName: String? = null,

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null,

        @SerializedName("favorite")
        @Expose
        var favorite: HashMap<String, String>? = null,

        @SerializedName("gender")
        @Expose
        var gender: String? = null,

        @SerializedName("image")
        @Expose
        var image: String? = null,

        @SerializedName("profession")
        @Expose
        var profession: String? = null,

        @SerializedName("email")
        @Expose
        var email: String? = null,

        @SerializedName("age")
        @Expose
        var age: Int? = null,

        @SerializedName("country")
        @Expose
        var country: String? = null,

        @SerializedName("height")
        @Expose
        var height: Int? = null,

        @SerializedName("id")
        @Expose
        var id: Int? = null
)