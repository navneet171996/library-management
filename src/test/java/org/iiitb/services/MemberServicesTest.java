package org.iiitb.services;

import org.iiitb.entities.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.iiitb.entities.Book;
import org.iiitb.services.MemberServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class MemberServicesTest {
    MemberServices memberServices = new MemberServices();
    Member member;
    Book book1, book2, book3, book4;

    @BeforeEach
    void setUp() {
        memberServices = new MemberServices();
        memberServices.bookCatalog = new HashMap<>();
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_Success() {
        // Arrange: Add books to the catalog
        memberServices.bookCatalog.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0));
        memberServices.bookCatalog.put(2, new Book(2, "Clean Code", "Robert C. Martin", "Programming", 0.0));

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("effective"));

        // Verify output
        assertTrue(output.contains("ID: 1, Title: Effective Java"));
        assertTrue(output.contains("ID: 2, Title: Clean Code"));
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_NoMatch() {
        // Arrange: Add books to the catalog
        memberServices.bookCatalog.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0));

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("python"));

        // Verify output
        assertTrue(output.contains("No books match your query."));
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_CaseInsensitive() {
        // Arrange: Add books to the catalog
        memberServices.bookCatalog.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0));

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("EFFECTIVE java"));

        // Verify output
        assertTrue(output.contains("ID: 1, Title: Effective Java"));
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_EmptyQuery() {
        // Arrange: Add books to the catalog
        memberServices.bookCatalog.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0));

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks(""));

        // Verify output: Since the query is empty, the method should return no results
        assertTrue(output.contains("No books match your query."));
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_MultipleMatches() {
        // Arrange: Add books to the catalog
        memberServices.bookCatalog.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0));
        memberServices.bookCatalog.put(2, new Book(2, "Clean Code", "Robert C. Martin", "Programming", 0.0));
        memberServices.bookCatalog.put(3, new Book(3, "Java Concurrency in Practice", "Brian Goetz", "Programming", 0.0));

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("java"));

        // Verify output
        assertTrue(output.contains("ID: 1, Title: Effective Java"));
        assertTrue(output.contains("ID: 2, Title: Clean Code"));
        assertTrue(output.contains("ID: 3, Title: Java Concurrency in Practice"));
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_MalformedBook() {
        // Arrange: Add a book with missing title and author
        memberServices.bookCatalog.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0));
        memberServices.bookCatalog.put(2, new Book(2, null, "Robert C. Martin", "Programming", 0.0)); // Missing title
        memberServices.bookCatalog.put(3, new Book(3, "Clean Code", null, "Programming", 0.0)); // Missing author

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("Clean"));

        // Verify output
        assertTrue(output.contains("ID: 1, Title: Effective Java"));
        assertTrue(output.contains("No books match your query.")); // The second book with missing title should not be printed.
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_ExceptionHandling() {
        // Arrange: Add a book with a null field to trigger an exception
        memberServices.bookCatalog.put(1, new Book(1, null, null, "Programming", 0.0));

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("java"));

        // Verify output
        assertTrue(output.contains("Error searching books:"));
    }

    // DU pair 201, 202, 203 are covered here
    @Test
    void testSearchBooks_LargeDataset() {
        // Arrange: Add 1000 books to the catalog
        for (int i = 0; i < 1000; i++) {
            memberServices.bookCatalog.put(i, new Book(i, "Title" + i, "Author" + i, "Genre" + i, 0.0));
        }

        // Capture output
        String output = captureOutput(() -> memberServices.searchBooks("Title500"));

        // Verify output
        assertTrue(output.contains("ID: 500, Title: Title500"));
    }

    /**
     * Helper method to capture system output during a method invocation.
     */
    private String captureOutput(Runnable methodUnderTest) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        methodUnderTest.run();
        System.setOut(System.out); // Restore original System.out
        return out.toString().trim();
    }

    // DU pair 204, 205, 206 are covered here
    @Test
    void testIssueBook_Success() {
        // Arrange: Set up books and a member
        member = new Member(1, "John Doe");  // Using constructor with id and name
        member.setIssuedBooks(new ArrayList<>());  // Manually setting the issuedBooks list
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0);
        memberServices.bookCatalog.put(1, book1);

        // Act: Issue the book
        memberServices.issueBook(member, 1);

        // Capture output
        String output = captureOutput(() -> memberServices.issueBook(member, 1));

        // Verify output
        assertTrue(output.contains("Book issued successfully!"));
        assertTrue(member.getIssuedBooks().contains("Effective Java"));
        assertTrue(book1.getIsIssued());
    }

    // DU pair 204, 205, 206 are covered here
    @Test
    void testIssueBook_BookAlreadyIssued() {
        // Arrange: Set up member and book
        member = new Member(1, "John Doe");  // Using constructor with id and name
        member.setIssuedBooks(new ArrayList<>());  // Manually setting the issuedBooks list
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 1.0); // Already issued
        memberServices.bookCatalog.put(1, book1);

        // Act: Attempt to issue an already issued book
        String output = captureOutput(() -> memberServices.issueBook(member, 1));

        // Verify output
        assertTrue(output.contains("Error: Book is already issued."));
    }

    // DU pair 204, 205, 206 are covered here
    @Test
    void testIssueBook_MemberAlreadyIssuedBook() {
        // Arrange: Set up member and book
        member = new Member(1, "John Doe");  // Using constructor with id and name
        member.setIssuedBooks(new ArrayList<>());  // Manually setting the issuedBooks list
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0);
        memberServices.bookCatalog.put(1, book1);
        memberServices.issueBook(member, 1); // Member already issued the book

        // Act: Attempt to issue the same book again
        String output = captureOutput(() -> memberServices.issueBook(member, 1));

        // Verify output
        assertTrue(output.contains("Error: You have already issued this book."));
    }

    // DU pair 204, 205, 206 are covered here
    @Test
    void testIssueBook_MaxLimitExceeded() {
        // Arrange: Set up member and books
        member = new Member(1, "John Doe");  // Using constructor with id and name
        member.setIssuedBooks(new ArrayList<>());  // Manually setting the issuedBooks list
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0);
        book2 = new Book(2, "Clean Code", "Robert C. Martin", "Programming", 0.0);
        book3 = new Book(3, "The Pragmatic Programmer", "Andrew Hunt", "Programming", 0.0);
        book4 = new Book(4, "Design Patterns", "Erich Gamma", "Design", 0.0);

        memberServices.bookCatalog.put(1, book1);
        memberServices.bookCatalog.put(2, book2);
        memberServices.bookCatalog.put(3, book3);
        memberServices.bookCatalog.put(4, book4);

        // Issue 3 books to the member
        memberServices.issueBook(member, 1);
        memberServices.issueBook(member, 2);
        memberServices.issueBook(member, 3);

        // Act: Try to issue a 4th book
        String output = captureOutput(() -> memberServices.issueBook(member, 4));

        // Verify output
        assertTrue(output.contains("Error: You cannot issue more than 3 books at a time."));
    }

    // DU pair 204, 205, 206 are covered here
    @Test
    void testIssueBook_BookNotFound() {
        // Arrange: Set up member
        member = new Member(1, "John Doe");  // Using constructor with id and name
        member.setIssuedBooks(new ArrayList<>());  // Manually setting the issuedBooks list

        // Act: Try to issue a non-existing book (id = 999)
        String output = captureOutput(() -> memberServices.issueBook(member, 999));

        // Verify output
        assertTrue(output.contains("Error: Book not found."));
    }

    // DU pair 204, 205, 206 are covered here
    @Test
    void testIssueBook_ExceptionHandling() {
        // Act: Simulate an error by passing a null member
        String output = captureOutput(() -> memberServices.issueBook(null, 1));

        // Verify output
        assertTrue(output.contains("Error issuing book:"));
    }

    @Test
    void testReturnBook_Success() {
        // Arrange: Set up member and books
        member = new Member(1, "John Doe");
        member.setIssuedBooks(new ArrayList<>());
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 1.0);  // Already issued
        memberServices.bookCatalog.put(1, book1);
        memberServices.issueBook(member, 1);  // Member issues the book

        // Act: Return the book
        String output = captureOutput(() -> memberServices.returnBook(member, 1));

        // Verify output
        assertTrue(output.contains("Book returned successfully!"));
        assertFalse(member.getIssuedBooks().contains("Effective Java"));
        assertFalse(book1.getIsIssued());
    }

    @Test
    void testReturnBook_BookNotIssued() {
        // Arrange: Set up member and book
        member = new Member(1, "John Doe");
        member.setIssuedBooks(new ArrayList<>());
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 0.0); // Book is not issued
        memberServices.bookCatalog.put(1, book1);

        // Act: Try to return a book that was not issued
        String output = captureOutput(() -> memberServices.returnBook(member, 1));

        // Verify output
        assertTrue(output.contains("Error: This book was not issued."));
    }

    @Test
    void testReturnBook_BookNotIssuedByMember() {
        // Arrange: Set up member and books
        member = new Member(1, "John Doe");
        member.setIssuedBooks(new ArrayList<>());
        book1 = new Book(1, "Effective Java", "Joshua Bloch", "Programming", 1.0);
        book2 = new Book(2, "Clean Code", "Robert C. Martin", "Programming", 1.0);
        memberServices.bookCatalog.put(1, book1);
        memberServices.bookCatalog.put(2, book2);
        memberServices.issueBook(member, 1);  // Member issues only book1

        // Act: Try to return a book that the member didn't issue
        String output = captureOutput(() -> memberServices.returnBook(member, 2));

        // Verify output
        assertTrue(output.contains("Error: You did not issue this book."));
    }

    @Test
    void testReturnBook_BookNotFound() {
        // Arrange: Set up member
        member = new Member(1, "John Doe");
        member.setIssuedBooks(new ArrayList<>());

        // Act: Try to return a non-existing book (id = 999)
        String output = captureOutput(() -> memberServices.returnBook(member, 999));

        // Verify output
        assertTrue(output.contains("Error: Book not found."));
    }

    @Test
    void testReturnBook_ExceptionHandling() {
        // Act: Simulate an error by passing a null member
        String output = captureOutput(() -> memberServices.returnBook(null, 1));

        // Verify output
        assertTrue(output.contains("Error returning book:"));
    }


}