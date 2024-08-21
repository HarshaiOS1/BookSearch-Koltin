package com.company.booksearch

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.company.booksearch.data.BookDatabase
import com.company.booksearch.model.Book
import com.company.booksearch.viewModel.BookViewModel
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BookViewModelTest {
    private lateinit var database: BookDatabase
    private lateinit var bookViewModel: BookViewModel
    val appContext = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        database = BookDatabase.getDatabase(appContext)
        bookViewModel = BookViewModel(database)
    }

    /**
     * Testing of book search operation
     * NOTE: Need to mock api request
     * */
    @Test
    fun testBookSearchOpearation() {
        //inital book count should be zero
        val count = bookViewModel.books.count()
        assertEquals(0, count)
        val latch = CountDownLatch(1)
        //IMP: Need to mock this api request
        bookViewModel.searchBooks("testing")
        latch.await(3, TimeUnit.SECONDS)
        //check if books are fetched and added to db
        assert(bookViewModel.books.isNotEmpty())
    }

    /**
     * Testing marking a book as favourite
     * */
    @Test
    fun testTogglingBookAsFavorite() {
        val latch = CountDownLatch(1)
        //IMP: Need to mock this api request
        bookViewModel.searchBooks("testing")
        latch.await(3, TimeUnit.SECONDS)
        val book = bookViewModel.filteredBooks.first()
        println(book)
        //check book is not marked favoruite
        assertFalse(book.isFavorite)
        bookViewModel.toggleFavorite(book)
        //for db update async value change
        Thread.sleep(500)
        val books = bookViewModel.filteredBooks
        assertTrue(books.first().isFavorite)
    }

    /**
     * Testing local filtering of books by author or title
     */
    @Test
    fun testFilterBySearchQuery() {
        val latch = CountDownLatch(1)
        //IMP: Need to mock this api request
        bookViewModel.searchBooks("testing")
        latch.await(3, TimeUnit.SECONDS)
        val initialCount = bookViewModel.filteredBooks.count()
        bookViewModel.filterSearchQuery = "amazon"
        val bookCount = bookViewModel.filteredBooks.count()
        assertNotEquals(initialCount, bookCount)
    }

}
