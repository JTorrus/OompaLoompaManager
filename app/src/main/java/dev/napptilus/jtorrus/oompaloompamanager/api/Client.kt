package dev.napptilus.jtorrus.oompaloompamanager.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton representation of Retrofit Client
 *
 * This class defines a Retrofit builder with our API's base URL and a Gson Converter to prepare a client.
 *
 * @author Javier Torrus
 */
class Client {
    companion object {
        private const val BASE_URL = "https://2q2woep105.execute-api.eu-west-1.amazonaws.com/"
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }

            return retrofit
        }
    }
}