package org.iiitb.services;

import org.iiitb.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServicesTest {

    private Map<Integer, Book> bookCatalog;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        bookCatalog = new HashMap<>();
        scanner = mock(Scanner.class); // Mock the Scanner
        library = new Library(bookCatalog, scanner); // Assuming Library has a constructor taking these
    }

    @Test
    void testAddBookSuccess() {
        // Mock user input
        when(scanner.nextInt()).thenReturn(1);
        when(scanner.nextLine()).thenReturn("") // Clear buffer
                .thenReturn("Effective Java") // Title
                .thenReturn("Joshua Bloch")   // Author
                .thenReturn("Programming");  // Genre
        when(scanner.nextDouble()).thenReturn(45.99);

        // Call the method
        library.addBook();

        // Verify book is added
        assertTrue(bookCatalog.containsKey(1));
        Book book = bookCatalog.get(1);
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("Programming", book.getGenre());
        assertEquals(45.99, book.getPrice());
    }

    @Test
    void testAddBookDuplicateID() {
        bookCatalog.put(1, new Book(1, "Existing Book", "Author", "Genre", 30.00));

        // Mock user input for duplicate ID
        when(scanner.nextInt()).thenReturn(1);
        when(scanner.nextLine()).thenReturn("") // Clear buffer
                .thenReturn("New Book") // Title
                .thenReturn("Author")  // Author
                .thenReturn("Genre");  // Genre
        when(scanner.nextDouble()).thenReturn(50.00);

        // Call the method
        library.addBook();

        // Verify book is not added and catalog size remains the same
        assertEquals(1, bookCatalog.size());
    }

    @Test
    void testAddBookMissingFields() {
        // Mock user input with an empty title
        when(scanner.nextInt()).thenReturn(2);
        when(scanner.nextLine()).thenReturn("") // Clear buffer
                .thenReturn("") // Empty Title
                .thenReturn("Author") // Author
                .thenReturn("Genre"); // Genre
        when(scanner.nextDouble()).thenReturn(25.00);

        // Call the method
        library.addBook();

        // Verify book is not added
        assertFalse(bookCatalog.containsKey(2));
    }
}