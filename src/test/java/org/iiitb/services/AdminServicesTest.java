package org.iiitb.services;

import org.iiitb.database.DatabaseServices;
import org.iiitb.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AdminServicesTest {

    private AdminServices adminServices;
    private Map<Integer, Book> bookCatalog;
    private DatabaseServices databaseServices;

    @BeforeEach
    void setUp() {
        bookCatalog = new HashMap<>(); // Use a real HashMap for testing
        databaseServices = Mockito.mock(DatabaseServices.class); // Mock DatabaseServices
        adminServices = new AdminServices(); // Inject dependencies
    }

    @Test
    void testAddBook_Success() {
        // Define test data
        int id = 4;
        String title = "Test Title";
        String author = "Test Author";
        String genre = "Fiction";
        double price = 199.99;

        // Call the method under test
        Book bookToBeAdded = new Book(id, title, author, genre, price);
        adminServices.addBook(bookToBeAdded);

        // Verify book was added to catalog
        assert(bookCatalog.containsKey(id));
        assert(bookCatalog.get(id).equals(bookToBeAdded));

        // Verify database service interaction
        verify(databaseServices, times(1)).addBookToDatabase(bookToBeAdded);
    }

//    @Test
//    void testAddBook_ExistingId() {
//        // Preload a book in the catalog
//        int id = 1;
//        Book existingBook = new Book(id, "Existing Title", "Existing Author", "Genre", 150.0);
//        bookCatalog.put(id, existingBook);
//
//        // Call the method with the same ID
//        bookService.addBook(id, "New Title", "New Author", "New Genre", 200.0);
//
//        // Verify that the existing book was not overwritten
//        assert(bookCatalog.get(id).equals(existingBook));
//
//        // Verify no interaction with the database service
//        verify(databaseServices, never()).addBookToDatabase(any());
//    }
//
//    @Test
//    void testAddBook_MissingFields() {
//        // Call the method with empty title
//        bookService.addBook(1, "", "Author", "Genre", 100.0);
//        // Call the method with empty author
//        bookService.addBook(2, "Title", "", "Genre", 100.0);
//        // Call the method with empty genre
//        bookService.addBook(3, "Title", "Author", "", 100.0);
//
//        // Verify no books were added to the catalog
//        assert(bookCatalog.isEmpty());
//
//        // Verify no interaction with the database service
//        verify(databaseServices, never()).addBookToDatabase(any());
//    }
//
//    @Test
//    void testAddBook_ExceptionHandling() {
//        // Simulate an exception when interacting with the database
//        int id = 1;
//        String title = "Test Title";
//        String author = "Test Author";
//        String genre = "Fiction";
//        double price = 199.99;
//
//        doThrow(new RuntimeException("Database Error"))
//                .when(databaseServices).addBookToDatabase(any());
//
//        // Call the method
//        bookService.addBook(id, title, author, genre, price);
//
//        // Verify the book was added to the catalog despite the exception
//        assert(bookCatalog.containsKey(id));
//
//        // Verify the exception was caught and the application did not crash
//        verify(databaseServices, times(1)).addBookToDatabase(any());
//    }
//}
}