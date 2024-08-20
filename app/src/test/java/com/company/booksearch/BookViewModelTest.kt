package com.company.booksearch

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.company.booksearch.data.BookDatabase
import com.company.booksearch.model.Book
import com.company.booksearch.model.dao.BookDao
import com.company.booksearch.network.apis.GoogleBooksApi
import com.company.booksearch.network.mappers.GoogleBooksResponse
import com.company.booksearch.viewModel.BookViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BookViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var database: BookDatabase
    @Mock
    private lateinit var bookDao: BookDao

    private lateinit var bookViewModel: BookViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // Mock the database and DAO
        `when`(database.bookDao()).thenReturn(bookDao)

        // Set the dispatcher for testing coroutines
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel with the mocked database
        bookViewModel = BookViewModel(database)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `loadBooksFromDatabase should load books and set the books list`() = runTest {
        // Arrange
        val bookList = listOf(
            Book(
                id = "1",
                title = "Book One",
                author = "Author One",
                description = "",
                imageUrl = "",
                isFavorite = false
            ),
            Book(
                id = "2",
                title = "Book Two",
                author = "Author Two",
                description = "",
                imageUrl = "",
                isFavorite = false
            )
        )
        `when`(bookDao.getAllBooks()).thenReturn(bookList)

        // Act
//        bookViewModel.loadBooksFromDatabase()

        // Assert
        Assert.assertEquals(bookList, bookViewModel.books)
        Assert.assertEquals("", bookViewModel.error)
    }

//    @Test
//    fun `searchBooks should fetch books from API and insert into database`() = runTest {
//        // Arrange
//        val query = "Android"
//        val googleBooksResponse = mock(GoogleBooksResponse::class.java)
//        `when`(googleBooksApi.searchBooks(query)).thenReturn(googleBooksResponse)
//        val mockBookList = listOf(
//            Book(id = "1", title = "Android Development", author = "Author A", description = "", imageUrl = "", isFavorite = false)
//        )
//        `when`(googleBooksResponse.items).thenReturn(mockBookList.map { it.toVolumeInfo() }) // Mock conversion
//
//        // Act
//        bookViewModel.searchBooks(query)
//
//        // Assert
//        verify(bookDao).insertBooks(mockBookList)
//        verify(bookDao, times(1)).getAllBooks()
//        Assert.assertEquals(mockBookList, bookViewModel.books)
//    }

    @Test
    fun `toggleShowFavourites should toggle the showFavourites flag`() {
        // Act
        bookViewModel.toggleShowFavourites()

        // Assert
        Assert.assertTrue(bookViewModel.showFavourites)

        // Act again
        bookViewModel.toggleShowFavourites()

        // Assert again
        Assert.assertFalse(bookViewModel.showFavourites)
    }

    @Test
    fun `toggleFavorite should update book's favorite status and update the list`() = runTest {
        // Arrange
        val book = Book(
            id = "1",
            title = "Book One",
            author = "Author One",
            description = "",
            imageUrl = "",
            isFavorite = false
        )
        `when`(bookDao.updateBook(book)).thenReturn(Unit)

        // Act
        bookViewModel.toggleFavorite(book)

        // Assert
        verify(bookDao).updateBook(book)
        Assert.assertTrue(book.isFavorite)
        Assert.assertTrue(bookViewModel.books.any { it.id == book.id && it.isFavorite })
    }

//    @Test
//    fun `selectBook should set selectedBook with the correct book`() = runTest {
//        // Arrange
//        val bookId = "1"
//        val book = Book(
//            id = bookId,
//            title = "Book One",
//            author = "Author One",
//            description = "",
//            imageUrl = "",
//            isFavorite = false
//        )
//        `when`(bookDao.getBookById(bookId)).thenReturn(book)
//
//        // Act
//        bookViewModel.selectBook(bookId)
//
//        // Assert
//        Assert.assertEquals(book, bookViewModel.selectedBook)
//    }
}