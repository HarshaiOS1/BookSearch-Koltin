package com.company.booksearch.network.apis

import com.company.booksearch.network.mappers.GoogleBooksResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface representing the Google Books API, used to fetch book data based on a search query.
 * This API client is built using Retrofit.
 */
interface GoogleBooksApi {
    /**
     * Searches for books using the Google Books API.
     *
     * @param query The search term used to query books.
     * @return A [GoogleBooksResponse] containing the list of books matching the query.
     */
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): GoogleBooksResponse

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"

        /**
         * Creates an instance of [GoogleBooksApi] configured with necessary interceptors
         * and converters for making API requests.
         *
         * @return An instance of [GoogleBooksApi].
         */
        fun create(): GoogleBooksApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Logs full request/response body
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoogleBooksApi::class.java)
        }
    }
}
