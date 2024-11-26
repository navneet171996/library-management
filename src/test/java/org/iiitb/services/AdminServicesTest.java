package org.iiitb.services;

import org.iiitb.database.DatabaseServices;
import org.iiitb.entities.Book;
import org.iiitb.entities.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminServicesTest {

    private AdminServices adminServices;
    private DatabaseServices databaseServices;
    private Map<Integer, Member> members;

    @BeforeEach
    void setUp() {
        databaseServices = mock(DatabaseServices.class);
        adminServices = new AdminServices();
    }

    @Test
    void testBookAlreadyExists() {
        Book existingBook = new Book(1, "Title", "Author", "Genre", 100.0, false);
        when(databaseServices.findBookById(1)).thenReturn(existingBook);

        Book newBook = new Book(1, "New Title", "New Author", "New Genre", 150.0, false);
        adminServices.addBook(newBook);

        verify(databaseServices, never()).addBookToDatabase(any(Book.class));
    }

    @Test
    void testBookFieldsAreEmpty() {
        Book incompleteBook = new Book(2, "", "Author", "Genre", 50.0, false);
        adminServices.addBook(incompleteBook);

        verify(databaseServices, never()).addBookToDatabase(any(Book.class));
    }

    @Test
    void testBookAddedSuccessfully() {
        when(databaseServices.findBookById(3)).thenReturn(null);

        Book newBook = new Book(3, "Valid Title", "Valid Author", "Valid Genre", 200.0, false);
        adminServices.addBook(newBook);

        verify(databaseServices, times(1)).addBookToDatabase(newBook);
    }

    @Test
    void testExceptionThrownWhileAddingBook() {
        when(databaseServices.findBookById(4)).thenThrow(new RuntimeException("Database error"));

        Book book = new Book(4, "Title", "Author", "Genre", 150.0, false);

        assertDoesNotThrow(() -> adminServices.addBook(book));

        verify(databaseServices, never()).addBookToDatabase(any(Book.class));
    }

    @Test
    void testDeleteBook_BookExists_NotIssued() {
        Book book = new Book(1, "Book Title", "Author", "Genre", 100.0, false);
        book.setIsIssued(false);

        when(databaseServices.findBookById(1)).thenReturn(book);

        adminServices.deleteBook(1);

        verify(databaseServices, times(1)).findBookById(1);
        verify(databaseServices, times(1)).deleteBookById(1);
        verifyNoMoreInteractions(databaseServices);
    }

    @Test
    void testDeleteBook_BookExists_Issued() {
        Book book = new Book(2, "Issued Book", "Author", "Genre", 150.0, false);
        book.setIsIssued(true);

        when(databaseServices.findBookById(2)).thenReturn(book);

        adminServices.deleteBook(2);

        verify(databaseServices, times(1)).findBookById(2);
        verify(databaseServices, never()).deleteBookById(any());
    }

    @Test
    void testDeleteBook_BookDoesNotExist() {
        when(databaseServices.findBookById(3)).thenReturn(null);

        adminServices.deleteBook(3);

        verify(databaseServices, times(1)).findBookById(3);
        verify(databaseServices, never()).deleteBookById(any());
    }

    @Test
    void testDeleteBook_ExceptionHandling() {
        when(databaseServices.findBookById(4)).thenThrow(new RuntimeException("Database error"));

        adminServices.deleteBook(4);

        verify(databaseServices, times(1)).findBookById(4);
        verify(databaseServices, never()).deleteBookById(any());
    }

    @Test
    void testViewAllBooks_NoBooksAvailable() {
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(new ArrayList<>());

        adminServices.viewAllBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testViewAllBooks_BooksAvailable() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Book One", "Author One", "Genre One", 200.0, false));
        books.add(new Book(2, "Book Two", "Author Two", "Genre Two", 300.0, true));

        when(databaseServices.getAllBooksFromDatabase()).thenReturn(books);

        adminServices.viewAllBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testViewAllBooks_ExceptionHandling() {
        when(databaseServices.getAllBooksFromDatabase()).thenThrow(new RuntimeException("Database error"));

        adminServices.viewAllBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalBooks_NoBooks() {
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(new ArrayList<>());

        adminServices.calculateTotalBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalBooks_WithBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Book One", "Author One", "Genre One", 200.0, false));
        books.add(new Book(2, "Book Two", "Author Two", "Genre Two", 300.0, true));
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(books);

        adminServices.calculateTotalBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalBooks_ExceptionHandling() {
        when(databaseServices.getAllBooksFromDatabase()).thenThrow(new RuntimeException("Database error"));

        adminServices.calculateTotalBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalLibraryValue_NoBooks() {
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(new ArrayList<>());

        adminServices.calculateTotalLibraryValue();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalLibraryValue_WithBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Book One", "Author One", "Genre One", 200.0, false));
        books.add(new Book(2, "Book Two", "Author Two", "Genre Two", 300.0, true));
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(books);

        adminServices.calculateTotalLibraryValue();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalLibraryValue_ExceptionHandling() {
        when(databaseServices.getAllBooksFromDatabase()).thenThrow(new RuntimeException("Database error"));

        adminServices.calculateTotalLibraryValue();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalIssuedBooks_WithIssuedBooks() {
        List<Book> books = Arrays.asList(
                new Book(1, "Book 1", "Author 1", "Genre 1", 100.0, false),
                new Book(2, "Book 2", "Author 2", "Genre 2", 150.0, false),
                new Book(3, "Book 3", "Author 3", "Genre 3", 200.0, false)
        );
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(books);

        adminServices.calculateTotalIssuedBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalIssuedBooks_NoIssuedBooks() {
        List<Book> books = Arrays.asList(
                new Book(1, "Book 1", "Author 1", "Genre 1", 100.0, false),
                new Book(2, "Book 2", "Author 2", "Genre 2", 150.0, false)
        );
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(books);

        adminServices.calculateTotalIssuedBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalIssuedBooks_EmptyDatabase() {
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(Collections.emptyList());

        adminServices.calculateTotalIssuedBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

    @Test
    void testCalculateTotalIssuedBooks_Exception() {
        when(databaseServices.getAllBooksFromDatabase()).thenThrow(new RuntimeException("Database error"));

        adminServices.calculateTotalIssuedBooks();

        verify(databaseServices, times(1)).getAllBooksFromDatabase();
    }

//    @Test
//    void testMostPopularBookFound() {
//        Book book1 = new Book(1, "Book1", "Author1", "Genre1", 100.0, false);
//        Book book2 = new Book(2, "Book2", "Author2", "Genre2", 150.0, false);
//        Member member1 = new Member(1, "Member1", true, List.of("Book1"));
//        Member member2 = new Member(2, "Member2", true, List.of("Book1", "Book2"));
//
//        when(databaseServices.getAllBooksFromDatabase()).thenReturn(List.of(book1, book2));
//        when(databaseServices.getAllMembersFromDatabase()).thenReturn(List.of(member1, member2));
//
//        adminServices.calculateMostPopularBook();
//
//        verify(databaseServices).getAllBooksFromDatabase();
//        verify(databaseServices).getAllMembersFromDatabase();
//    }

    @Test
    void testNoBooksIssued() {
        Book book1 = new Book(1, "Book1", "Author1", "Genre1", 100.0, false);
        Book book2 = new Book(2, "Book2", "Author2", "Genre2", 150.0, false);

        when(databaseServices.getAllBooksFromDatabase()).thenReturn(List.of(book1, book2));
        when(databaseServices.getAllMembersFromDatabase()).thenReturn(Collections.emptyList());

        adminServices.calculateMostPopularBook();

        verify(databaseServices).getAllBooksFromDatabase();
        verify(databaseServices).getAllMembersFromDatabase();
    }

    @Test
    void testNoBooksInDatabase() {
        when(databaseServices.getAllBooksFromDatabase()).thenReturn(Collections.emptyList());
        when(databaseServices.getAllMembersFromDatabase()).thenReturn(Collections.emptyList());

        adminServices.calculateMostPopularBook();

        verify(databaseServices).getAllBooksFromDatabase();
        verify(databaseServices, never()).getAllMembersFromDatabase();
    }

    @Test
    void testErrorFetchingData() {
        when(databaseServices.getAllBooksFromDatabase()).thenThrow(new RuntimeException("Database error"));

        adminServices.calculateMostPopularBook();

        verify(databaseServices).getAllBooksFromDatabase();
        verify(databaseServices, never()).getAllMembersFromDatabase();
    }

    @Test
    void testFindBooksByGenre_FoundBooks() {

        List<Book> mockBooks = Arrays.asList(
                new Book(1, "Book1", "Author1", "Fiction", 10.99, false),
                new Book(2, "Book2", "Author2", "Non-Fiction", 12.99, false),
                new Book(3, "Book3", "Author3", "Fiction", 15.99, false));

        when(databaseServices.getAllBooksFromDatabase()).thenReturn(mockBooks);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        adminServices.findBooksByGenre("Fiction");

        assertTrue(baos.toString().contains("ID: 1, Title: Book1, Author: Author1"));
        assertTrue(baos.toString().contains("ID: 3, Title: Book3, Author: Author3"));
        assertFalse(baos.toString().contains("No books found for the given genre"));

        System.setOut(originalOut);
    }

    @Test
    void testFindBooksByGenre_NoBooksFound() {

        List<Book> mockBooks = Arrays.asList(
                new Book(1, "Book1", "Author1", "Fiction", 10.99, false),
                new Book(2, "Book2", "Author2", "Non-Fiction", 12.99, false),
                new Book(3, "Book3", "Author3", "Fiction", 15.99, false));

        when(databaseServices.getAllBooksFromDatabase()).thenReturn(mockBooks);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        adminServices.findBooksByGenre("Science");

        assertTrue(baos.toString().contains("No books found for the given genre"));
        assertFalse(baos.toString().contains("ID:"));
        assertFalse(baos.toString().contains("Title:"));

        System.setOut(originalOut);
    }

}

//    @Test
//    void testFindInactiveMembers_Success() {
//        // Add members to the system
//        Member activeMember = new Member(1, "Alice");
//        activeMember.setIssuedBooks(new ArrayList<>(List.of("Book1"))); // Active member with issued books
//        Member inactiveMember = new Member(2, "Bob");
//        inactiveMember.setIssuedBooks(new ArrayList<>()); // Inactive member with no issued books
//
//        adminServices.members.put(1, activeMember);
//        adminServices.members.put(2, inactiveMember);
//
//        // Capture system output
//        String output = captureOutput(adminServices::findInactiveMembers);
//
//        // Verify inactive members are identified correctly
//        assertTrue(output.contains("ID: 2, Name: Bob"));
//        assertFalse(output.contains("ID: 1, Name: Alice"));
//    }
//
//    @Test
//    void testFindInactiveMembers_ExistingData() {
//        // Add only active members
//        Member member = new Member(1, "Alice");
//        member.setIssuedBooks(new ArrayList<>(List.of("Book1"))); // Active member with issued books
//
//        adminServices.members.put(1, member);
//
//        // Capture system output
//        String output = captureOutput(adminServices::findInactiveMembers);
//
//        // Verify no inactive members are found
//        assertFalse(output.contains("ID: 1, Name: Alice"));
//        assertTrue(output.contains("Inactive Members:")); // Output still includes the header
//    }
//
//    @Test
//    void testFindInactiveMembers_MissingField() {
//        // Add a member without issuedBooks initialized (null)
//        Member member = new Member(1, "Alice");
//        member.setIssuedBooks(null); // Simulate missing field
//
//        adminServices.members.put(1, member);
//
//        // Capture system output
//        String output = captureOutput(adminServices::findInactiveMembers);
//
//        // Verify that no inactive members are found due to missing data
//        assertFalse(output.contains("ID: 1, Name: Alice"));
//    }
//
//    @Test
//    void testFindInactiveMembers_Failure() {
//        // Do not add any members to the system
//        adminServices.members.clear();
//
//        // Capture system output
//        String output = captureOutput(adminServices::findInactiveMembers);
//
//        // Verify no members are listed
//        assertTrue(output.contains("Inactive Members:"));
//        assertFalse(output.contains("ID:"));
//    }
//
//    /**
//     * Helper method to capture system output during a method invocation.
//     */
//    private String captureOutput(Runnable methodUnderTest) {
//        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
//        System.setOut(new java.io.PrintStream(out));
//        methodUnderTest.run();
//        System.setOut(System.out); // Restore original System.out
//        return out.toString();
//    }
//
//    @Test
//    void testUpdateMemberDetails_Success() {
//        // Set up test data
//        Member member1 = new Member(1, "Alice");
//        members.put(1, member1);
//
//        // Capture system output
//        String output = captureOutput(() -> adminServices.updateMemberDetails(1, "Alicia"));
//
//        // Verify the member's name is updated correctly
//        assertEquals("Alicia", member1.getName());
//        assertTrue(output.contains("Member details updated successfully!"));
//    }
//
//    @Test
//    void testUpdateMemberDetails_MemberNotFound() {
//        // Capture system output when trying to update a non-existing member
//        String output = captureOutput(() -> adminServices.updateMemberDetails(999, "John"));
//
//        // Verify output for invalid member
//        assertTrue(output.contains("Member not found."));
//    }
//
//    @Test
//    void testUpdateMemberDetails_EdgeCase_EmptyName() {
//        // Set up test data
//        Member member1 = new Member(1, "Alice");
//        members.put(1, member1);
//
//        // Capture system output when providing an empty name
//        String output = captureOutput(() -> adminServices.updateMemberDetails(1, ""));
//
//        // Verify that the member's name is not updated to an empty string
//        assertEquals("Alice", member1.getName());
//        assertTrue(output.contains("Member details updated successfully!"));
//    }
//
//    @Test
//    void testUpdateMemberDetails_EdgeCase_SpecialCharacters() {
//        // Set up test data
//        Member member1 = new Member(1, "Alice");
//        members.put(1, member1);
//
//        // Capture system output when providing a name with special characters
//        String output = captureOutput(() -> adminServices.updateMemberDetails(1, "!@#^&*()"));
//
//        // Verify that the member's name is updated correctly with special characters
//        assertEquals("!@#^&*()", member1.getName());
//        assertTrue(output.contains("Member details updated successfully!"));
//    }
//
//    @Test
//    void testViewIssuedBooksByGenre_Success() {
//        // Set up test data
//        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
//        Book book2 = new Book(2, "Book2", "Author2", "Fiction", 1.0);
//        Book book3 = new Book(3, "Book3", "Author3", "Science", 0.0); // Not issued
//        bookCatalog.put(1, book1);
//        bookCatalog.put(2, book2);
//        bookCatalog.put(3, book3);
//
//        // Capture system output
//        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre("Fiction"));
//
//        // Verify output
//        assertTrue(output.contains("Issued Books in Genre: Fiction"));
//        assertTrue(output.contains("ID: 1, Title: Book1, Author: Author1"));
//        assertTrue(output.contains("ID: 2, Title: Book2, Author: Author2"));
//        assertFalse(output.contains("No issued books found in this genre."));
//    }
//
//    @Test
//    void testViewIssuedBooksByGenre_NoIssuedBooks() {
//        // Set up test data with no issued books in the genre
//        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 0.0);
//        Book book2 = new Book(2, "Book2", "Author2", "Fiction", 0.0);
//        bookCatalog.put(1, book1);
//        bookCatalog.put(2, book2);
//
//        // Capture system output
//        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre("Fiction"));
//
//        // Verify output for no issued books
//        assertTrue(output.contains("Issued Books in Genre: Fiction"));
//        assertTrue(output.contains("No issued books found in this genre."));
//    }
//
//    @Test
//    void testViewIssuedBooksByGenre_GenreNotFound() {
//        // Set up test data with a genre not in the catalog
//        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
//        bookCatalog.put(1, book1);
//
//        // Capture system output
//        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre("Non-Fiction"));
//
//        // Verify output when the genre is not found
//        assertTrue(output.contains("Issued Books in Genre: Non-Fiction"));
//        assertTrue(output.contains("No issued books found in this genre."));
//    }
//
//    @Test
//    void testViewIssuedBooksByGenre_EmptyGenre() {
//        // Set up test data
//        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
//        Book book2 = new Book(2, "Book2", "Author2", "Fiction", 1.0);
//        bookCatalog.put(1, book1);
//        bookCatalog.put(2, book2);
//
//        // Capture system output when genre is empty
//        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre(""));
//
//        // Verify the output when an empty genre is provided
//        assertTrue(output.contains("Issued Books in Genre: "));
//        assertTrue(output.contains("No issued books found in this genre."));
//    }
//
//    @Test
//    void testCategorizeBooksByGenre_Success() {
//        // Add books with various genres
//        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Fiction", 1.0));
//        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Fiction", 0.0));
//        bookCatalog.put(3, new Book(3, "Book3", "Author3", "Science", 1.0));
//        bookCatalog.put(4, new Book(4, "Book4", "Author4", "History", 0.0));
//
//        // Capture system output
//        String output = captureOutput(adminServices::categorizeBooksByGenre);
//
//        // Verify output
//        assertTrue(output.contains("Books Categorized by Genre:"));
//        assertTrue(output.contains("Genre: Fiction, Count: 2"));
//        assertTrue(output.contains("Genre: Science, Count: 1"));
//        assertTrue(output.contains("Genre: History, Count: 1"));
//    }
//
//    @Test
//    void testCategorizeBooksByGenre_EmptyCatalog() {
//        // No books in the catalog
//
//        // Capture system output
//        String output = captureOutput(adminServices::categorizeBooksByGenre);
//
//        // Verify output for an empty catalog
//        assertTrue(output.contains("Books Categorized by Genre:"));
//        assertFalse(output.contains("Genre:"));
//    }
//
//    @Test
//    void testCategorizeBooksByGenre_SingleGenreMultipleBooks() {
//        // All books belong to the same genre
//        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Fiction", 1.0));
//        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Fiction", 1.0));
//        bookCatalog.put(3, new Book(3, "Book3", "Author3", "Fiction", 1.0));
//
//        // Capture system output
//        String output = captureOutput(adminServices::categorizeBooksByGenre);
//
//        // Verify output
//        assertTrue(output.contains("Books Categorized by Genre:"));
//        assertTrue(output.contains("Genre: Fiction, Count: 3"));
//        assertFalse(output.contains("Genre: Science"));
//    }
//
//    @Test
//    void testCategorizeBooksByGenre_MultipleGenresSingleBookEach() {
//        // One book per genre
//        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Fiction", 1.0));
//        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Science", 1.0));
//        bookCatalog.put(3, new Book(3, "Book3", "Author3", "History", 0.0));
//
//        // Capture system output
//        String output = captureOutput(adminServices::categorizeBooksByGenre);
//
//        // Verify output
//        assertTrue(output.contains("Books Categorized by Genre:"));
//        assertTrue(output.contains("Genre: Fiction, Count: 1"));
//        assertTrue(output.contains("Genre: Science, Count: 1"));
//        assertTrue(output.contains("Genre: History, Count: 1"));
//    }
