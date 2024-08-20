package com.company.booksearch.data

import android.content.Context
import androidx.room.*
import com.company.booksearch.model.Book
import com.company.booksearch.model.dao.BookDao

/**
 * Provides access to the BookDao interface for performing database operations.
 *
 * @return An instance of the BookDao.
 */
@Database(entities = [Book::class], version = 1)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        /**
         * Returns a singleton instance of the BookDatabase.
         *
         * @param context The application context used to create the database instance.
         * @return The singleton instance of BookDatabase.
         */
        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}