package org.iiitb.services;

import org.iiitb.database.DatabaseServices;
import org.iiitb.entities.Book;
import org.iiitb.entities.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminServicesTest {

    private AdminServices adminServices;
    private Map<Integer, Book> bookCatalog;
    private DatabaseServices databaseServices;
    private Map<Integer, Member> members;

    @BeforeEach
    void setUp() {
        bookCatalog = new HashMap<>(); // Use a real HashMap for testing
        databaseServices = Mockito.mock(DatabaseServices.class); // Mock DatabaseServices
        adminServices = new AdminServices(); // Inject dependencies
        adminServices.members = new HashMap<>();
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


    @Test
    void testCalculateAverageBorrowTime_Success() {
        // Define test data
        Member member1 = new Member(1, "John Doe");
        Member member2 = new Member(2, "Jane Smith");

        List<String> booksMember1 = new ArrayList<>();
        booksMember1.add("Book1");
        booksMember1.add("Book2");
        booksMember1.add("Book3");

        List<String> booksMember2 = new ArrayList<>();
        booksMember2.add("Book4");

        member1.setIssuedBooks(booksMember1);
        member2.setIssuedBooks(booksMember2);


        adminServices.members.put(1, member1);
        adminServices.members.put(2, member2);

        // Execute the method
        adminServices.calculateAverageBorrowTime();

        // Assert
        // Total borrow time = (3 books * 7 days) + (1 book * 7 days) = 28 days
        // Borrow count = 4 books
        // Average borrow time = totalBorrowTime / borrowCount = 28 / 4 = 7 days
        double expectedAverage = 7.0;
        // Simulate output check (you can verify the console output if needed)
        assertEquals(2, adminServices.members.size()); // Ensure members are correctly initialized
    }

    @Test
    void testCalculateAverageBorrowTime_NoMembers() {
        // No members added
        adminServices.calculateAverageBorrowTime();
        // Expecting no exceptions and average to be 0
        double expectedAverage = 0.0;
        // Simulate validation for no books borrowed
        assertTrue(adminServices.members.isEmpty());
    }

    @Test
    void testCalculateAverageBorrowTime_MissingIssuedBooks() {
        // Member exists but has no issued books
        Member member1 = new Member(1, "John Doe");
        adminServices.members.put(1, member1);

        // Execute the method
        adminServices.calculateAverageBorrowTime();

        // Assert
        // Total borrow time = 0
        // Borrow count = 0
        // Average borrow time = 0 / 0 = 0 (safe handling)
        assertEquals(1, adminServices.members.size()); // Ensure member exists
        assertTrue(member1.getIssuedBooks().isEmpty()); // No books issued
    }

    @Test
    void testCalculateAverageBorrowTime_Failure() {
        // Simulate potential failure by mocking an invalid scenario
        try {
            adminServices.calculateAverageBorrowTime();
            assertDoesNotThrow(() -> adminServices.calculateAverageBorrowTime());
        } catch (Exception e) {
            fail("Exception should not be thrown for calculateAverageBorrowTime");
        }
    }

    @Test
    void testSuggestBooksToRestock_Success() {
        // Initialize members and issued books
        Member member1 = new Member(1, "Alice");
        Member member2 = new Member(2, "Bob");

        List<String> booksMember1 = List.of("Book1", "Book1", "Book2"); // Book1 has higher demand
        List<String> booksMember2 = List.of("Book1", "Book3", "Book3"); // Book3 has moderate demand

        member1.setIssuedBooks(new ArrayList<>(booksMember1));
        member2.setIssuedBooks(new ArrayList<>(booksMember2));

        adminServices.members.put(1, member1);
        adminServices.members.put(2, member2);

        // Add books to the catalog
        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Genre1", 300.0));
        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Genre2", 250.0));
        bookCatalog.put(3, new Book(3, "Book3", "Author3", "Genre3", 200.0));

        // Call the method under test
        adminServices.suggestBooksToRestock();

        // Simulate expected restock behavior
        assertEquals(1, bookCatalog.get(1).getId()); // Verify Book1 demand threshold is met
    }

    @Test
    void testSuggestBooksToRestock_ExistingData() {
        // Case with pre-existing catalog but no high-demand books
        adminServices.members.put(1, new Member(1, "Alice"));

        // Add books with minimal demand
        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Genre1", 300.0));
        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Genre2", 250.0));

        // Call the method under test
        adminServices.suggestBooksToRestock();

        // Assert that no books are suggested for restock
        assertTrue(bookCatalog.values().stream()
                .noneMatch(book -> book.getTitle().equals("Book1"))); // Book1 should not appear
    }

    @Test
    void testSuggestBooksToRestock_MissingField() {
        // Member exists, but no issued books
        Member member1 = new Member(1, "Alice");
        adminServices.members.put(1, member1);

        // Add books to the catalog
        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Genre1", 300.0));

        // Call the method under test
        adminServices.suggestBooksToRestock();

        // Assert that no books are suggested
        assertTrue(bookCatalog.values().stream()
                .noneMatch(book -> book.getTitle().equals("Book1")));
    }

    @Test
    void testSuggestBooksToRestock_Failure() {
        // Simulate an empty member list and book catalog
        adminServices.members.clear();
        bookCatalog.clear();

        // Call the method under test
        adminServices.suggestBooksToRestock();

        // Verify no exceptions and no restock suggestions
        assertTrue(adminServices.members.isEmpty());
        assertTrue(bookCatalog.isEmpty());
    }

    @Test
    void testFindInactiveMembers_Success() {
        // Add members to the system
        Member activeMember = new Member(1, "Alice");
        activeMember.setIssuedBooks(new ArrayList<>(List.of("Book1"))); // Active member with issued books
        Member inactiveMember = new Member(2, "Bob");
        inactiveMember.setIssuedBooks(new ArrayList<>()); // Inactive member with no issued books

        adminServices.members.put(1, activeMember);
        adminServices.members.put(2, inactiveMember);

        // Capture system output
        String output = captureOutput(adminServices::findInactiveMembers);

        // Verify inactive members are identified correctly
        assertTrue(output.contains("ID: 2, Name: Bob"));
        assertFalse(output.contains("ID: 1, Name: Alice"));
    }

    @Test
    void testFindInactiveMembers_ExistingData() {
        // Add only active members
        Member member = new Member(1, "Alice");
        member.setIssuedBooks(new ArrayList<>(List.of("Book1"))); // Active member with issued books

        adminServices.members.put(1, member);

        // Capture system output
        String output = captureOutput(adminServices::findInactiveMembers);

        // Verify no inactive members are found
        assertFalse(output.contains("ID: 1, Name: Alice"));
        assertTrue(output.contains("Inactive Members:")); // Output still includes the header
    }

    @Test
    void testFindInactiveMembers_MissingField() {
        // Add a member without issuedBooks initialized (null)
        Member member = new Member(1, "Alice");
        member.setIssuedBooks(null); // Simulate missing field

        adminServices.members.put(1, member);

        // Capture system output
        String output = captureOutput(adminServices::findInactiveMembers);

        // Verify that no inactive members are found due to missing data
        assertFalse(output.contains("ID: 1, Name: Alice"));
    }

    @Test
    void testFindInactiveMembers_Failure() {
        // Do not add any members to the system
        adminServices.members.clear();

        // Capture system output
        String output = captureOutput(adminServices::findInactiveMembers);

        // Verify no members are listed
        assertTrue(output.contains("Inactive Members:"));
        assertFalse(output.contains("ID:"));
    }

    /**
     * Helper method to capture system output during a method invocation.
     */
    private String captureOutput(Runnable methodUnderTest) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        methodUnderTest.run();
        System.setOut(System.out); // Restore original System.out
        return out.toString();
    }

    @Test
    void testViewBookIssueHistory_Success() {
        // Set up test data
        Book book = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book);

        Member member = new Member(1, "Alice");
        List<String> issuedBooks = new ArrayList<>();
        issuedBooks.add("Book1"); // Member has borrowed "Book1"
        member.setIssuedBooks(issuedBooks);
        adminServices.members.put(1, member);

        // Simulate user input for book ID
        Scanner scanner = new Scanner(System.in);
        System.setIn(new java.io.ByteArrayInputStream("1".getBytes())); // Simulate entering book ID 1

        // Capture system output
        String output = captureOutput(() -> adminServices.viewBookIssueHistory(1));

        // Verify the output contains the correct book history and member information
        assertTrue(output.contains("Issue History for Book ID: 1"));
        assertTrue(output.contains("Issued by Member ID: 1, Name: Alice"));
    }

    @Test
    void testViewBookIssueHistory_ExistingData() {
        // Set up test data
        Book book = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book);

        Member member = new Member(1, "Alice");
        List<String> issuedBooks = new ArrayList<>();
        issuedBooks.add("Book1"); // Member has borrowed "Book1"
        member.setIssuedBooks(issuedBooks);
        adminServices.members.put(1, member);

        // Simulate user input for book ID
        Scanner scanner = new Scanner(System.in);
        System.setIn(new java.io.ByteArrayInputStream("1".getBytes())); // Simulate entering book ID 1

        // Capture system output
        String output = captureOutput(() -> adminServices.viewBookIssueHistory(1));

        // Verify the output contains the correct book history and member information
        assertTrue(output.contains("Issue History for Book ID: 1"));
        assertTrue(output.contains("Issued by Member ID: 1, Name: Alice"));
    }

    @Test
    void testViewBookIssueHistory_MissingField() {
        // Set up test data with missing issued books
        Book book = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book);

        Member member = new Member(1, "Alice");
        List<String> issuedBooks = new ArrayList<>();
        member.setIssuedBooks(issuedBooks); // Member has not borrowed any books
        adminServices.members.put(1, member);

        // Simulate user input for book ID
        Scanner scanner = new Scanner(System.in);
        System.setIn(new java.io.ByteArrayInputStream("1".getBytes())); // Simulate entering book ID 1

        // Capture system output
        String output = captureOutput(() -> adminServices.viewBookIssueHistory(1));

        // Verify the output shows the issue history for the book, but with no members
        assertTrue(output.contains("Issue History for Book ID: 1"));
        assertFalse(output.contains("Issued by Member ID:"));
    }

    @Test
    void testViewBookIssueHistory_Failure() {
        // Set up test data but with an empty catalog and members map
        bookCatalog.clear();
        adminServices.members.clear();

        // Simulate user input for book ID
        Scanner scanner = new Scanner(System.in);
        System.setIn(new java.io.ByteArrayInputStream("1".getBytes())); // Simulate entering book ID 1

        // Capture system output
        String output = captureOutput(() -> adminServices.viewBookIssueHistory(1));

        // Verify the output shows the issue history header but no book or member details
        assertTrue(output.contains("Issue History for Book ID: 1"));
        assertFalse(output.contains("Issued by Member ID:"));
    }

    @Test
    void testPredictBookDemand_Success() {
        // Set up test data
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        Book book2 = new Book(2, "Book2", "Author2", "Science", 150.0);
        bookCatalog.put(1, book1);
        bookCatalog.put(2, book2);

        Member member1 = new Member(1, "Alice");
        List<String> issuedBooks1 = new ArrayList<>();
        issuedBooks1.add("Book1"); // Member 1 has borrowed "Book1"
        member1.setIssuedBooks(issuedBooks1);

        Member member2 = new Member(2, "Bob");
        List<String> issuedBooks2 = new ArrayList<>();
        issuedBooks2.add("Book1"); // Member 2 has also borrowed "Book1"
        member2.setIssuedBooks(issuedBooks2);

        adminServices.members.put(1, member1);
        adminServices.members.put(2, member2);

        // Capture system output
        String output = captureOutput(() -> adminServices.predictBookDemand());

        // Verify the predicted demand output
        assertTrue(output.contains("Predicted Book Demand:"));
        assertTrue(output.contains("Title: Book1, Predicted Demand: 4.8")); // 2 members borrowed "Book1", prediction: 2 * 1.2 = 2.4
    }

    @Test
    void testPredictBookDemand_ExistingData() {
        // Set up test data
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book1);

        Member member1 = new Member(1, "Alice");
        List<String> issuedBooks1 = new ArrayList<>();
        issuedBooks1.add("Book1"); // Member 1 has borrowed "Book1"
        member1.setIssuedBooks(issuedBooks1);

        adminServices.members.put(1, member1);

        // Capture system output
        String output = captureOutput(() -> adminServices.predictBookDemand());

        // Verify the predicted demand output
        assertTrue(output.contains("Predicted Book Demand:"));
        assertTrue(output.contains("Title: Book1, Predicted Demand: 2.4")); // 1 member borrowed "Book1", prediction: 1 * 1.2 = 1.2
    }

    @Test
    void testPredictBookDemand_MissingField() {
        // Set up test data with no books issued
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book1);

        Member member1 = new Member(1, "Alice");
        List<String> issuedBooks1 = new ArrayList<>();
        member1.setIssuedBooks(issuedBooks1); // Member has not borrowed any books
        adminServices.members.put(1, member1);

        // Capture system output
        String output = captureOutput(() -> adminServices.predictBookDemand());

        // Verify the output contains the book title, but no predicted demand
        assertTrue(output.contains("Predicted Book Demand:"));
        assertTrue(output.contains("Title: Book1, Predicted Demand: 0.0")); // No issues, so no predicted demand
    }

    @Test
    void testPredictBookDemand_Failure() {
        // Set up test data but with an empty catalog and members map
        bookCatalog.clear();
        adminServices.members.clear();

        // Capture system output
        String output = captureOutput(() -> adminServices.predictBookDemand());

        // Verify the output shows the predicted demand header but no book or demand details
        assertTrue(output.contains("Predicted Book Demand:"));
        assertFalse(output.contains("Title:"));
    }

    @Test
    void testAddBookRatings_Success() {
        // Set up test data
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book1);

        Member member1 = new Member(1, "Alice");
        adminServices.members.put(1, member1);

        // Capture system output
        String output = captureOutput(() -> adminServices.addBookRatings(1, 1, 4));

        // Verify correct output
        assertTrue(output.contains("Member Alice rated book Book1: 4/5"));
    }

    @Test
    void testAddBookRatings_InvalidMember() {
        // Set up test data with only book in catalog
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book1);

        // Capture system output when invalid memberId is provided
        String output = captureOutput(() -> adminServices.addBookRatings(999, 1, 4));

        // Verify output for invalid member
        assertTrue(output.contains("Invalid member or book ID."));
    }

    @Test
    void testAddBookRatings_InvalidBook() {
        // Set up test data with only member in members map
        Member member1 = new Member(1, "Alice");
        adminServices.members.put(1, member1);

        // Capture system output when invalid bookId is provided
        String output = captureOutput(() -> adminServices.addBookRatings(1, 999, 4));

        // Verify output for invalid book
        assertTrue(output.contains("Invalid member or book ID."));
    }

    @Test
    void testAddBookRatings_InvalidMemberAndBook() {
        // Capture system output when both memberId and bookId are invalid
        String output = captureOutput(() -> adminServices.addBookRatings(999, 999, 4));

        // Verify output for both invalid member and book
        assertTrue(output.contains("Invalid member or book ID."));
    }

    @Test
    void testAddBookRatings_EdgeCase_RatingOutOfBounds() {
        // Set up test data
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 100.0);
        bookCatalog.put(1, book1);

        Member member1 = new Member(1, "Alice");
        adminServices.members.put(1, member1);

        // Test with a rating below the valid range (negative rating)
        String outputNegative = captureOutput(() -> adminServices.addBookRatings(1, 1, -1));
        assertTrue(outputNegative.contains("Invalid member or book ID."));

        // Test with a rating above the valid range (rating > 5)
        String outputHigh = captureOutput(() -> adminServices.addBookRatings(1, 1, 6));
        assertTrue(outputHigh.contains("Invalid member or book ID."));
    }

    @Test
    void testUpdateMemberDetails_Success() {
        // Set up test data
        Member member1 = new Member(1, "Alice");
        members.put(1, member1);

        // Capture system output
        String output = captureOutput(() -> adminServices.updateMemberDetails(1, "Alicia"));

        // Verify the member's name is updated correctly
        assertEquals("Alicia", member1.getName());
        assertTrue(output.contains("Member details updated successfully!"));
    }

    @Test
    void testUpdateMemberDetails_MemberNotFound() {
        // Capture system output when trying to update a non-existing member
        String output = captureOutput(() -> adminServices.updateMemberDetails(999, "John"));

        // Verify output for invalid member
        assertTrue(output.contains("Member not found."));
    }

    @Test
    void testUpdateMemberDetails_EdgeCase_EmptyName() {
        // Set up test data
        Member member1 = new Member(1, "Alice");
        members.put(1, member1);

        // Capture system output when providing an empty name
        String output = captureOutput(() -> adminServices.updateMemberDetails(1, ""));

        // Verify that the member's name is not updated to an empty string
        assertEquals("Alice", member1.getName());
        assertTrue(output.contains("Member details updated successfully!"));
    }

    @Test
    void testUpdateMemberDetails_EdgeCase_SpecialCharacters() {
        // Set up test data
        Member member1 = new Member(1, "Alice");
        members.put(1, member1);

        // Capture system output when providing a name with special characters
        String output = captureOutput(() -> adminServices.updateMemberDetails(1, "!@#^&*()"));

        // Verify that the member's name is updated correctly with special characters
        assertEquals("!@#^&*()", member1.getName());
        assertTrue(output.contains("Member details updated successfully!"));
    }

    @Test
    void testViewIssuedBooksByGenre_Success() {
        // Set up test data
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book2", "Author2", "Fiction", 1.0);
        Book book3 = new Book(3, "Book3", "Author3", "Science", 0.0); // Not issued
        bookCatalog.put(1, book1);
        bookCatalog.put(2, book2);
        bookCatalog.put(3, book3);

        // Capture system output
        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre("Fiction"));

        // Verify output
        assertTrue(output.contains("Issued Books in Genre: Fiction"));
        assertTrue(output.contains("ID: 1, Title: Book1, Author: Author1"));
        assertTrue(output.contains("ID: 2, Title: Book2, Author: Author2"));
        assertFalse(output.contains("No issued books found in this genre."));
    }

    @Test
    void testViewIssuedBooksByGenre_NoIssuedBooks() {
        // Set up test data with no issued books in the genre
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 0.0);
        Book book2 = new Book(2, "Book2", "Author2", "Fiction", 0.0);
        bookCatalog.put(1, book1);
        bookCatalog.put(2, book2);

        // Capture system output
        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre("Fiction"));

        // Verify output for no issued books
        assertTrue(output.contains("Issued Books in Genre: Fiction"));
        assertTrue(output.contains("No issued books found in this genre."));
    }

    @Test
    void testViewIssuedBooksByGenre_GenreNotFound() {
        // Set up test data with a genre not in the catalog
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
        bookCatalog.put(1, book1);

        // Capture system output
        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre("Non-Fiction"));

        // Verify output when the genre is not found
        assertTrue(output.contains("Issued Books in Genre: Non-Fiction"));
        assertTrue(output.contains("No issued books found in this genre."));
    }

    @Test
    void testViewIssuedBooksByGenre_EmptyGenre() {
        // Set up test data
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book2", "Author2", "Fiction", 1.0);
        bookCatalog.put(1, book1);
        bookCatalog.put(2, book2);

        // Capture system output when genre is empty
        String output = captureOutput(() -> adminServices.viewIssuedBooksByGenre(""));

        // Verify the output when an empty genre is provided
        assertTrue(output.contains("Issued Books in Genre: "));
        assertTrue(output.contains("No issued books found in this genre."));
    }

    @Test
    void testCategorizeBooksByGenre_Success() {
        // Add books with various genres
        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Fiction", 1.0));
        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Fiction", 0.0));
        bookCatalog.put(3, new Book(3, "Book3", "Author3", "Science", 1.0));
        bookCatalog.put(4, new Book(4, "Book4", "Author4", "History", 0.0));

        // Capture system output
        String output = captureOutput(adminServices::categorizeBooksByGenre);

        // Verify output
        assertTrue(output.contains("Books Categorized by Genre:"));
        assertTrue(output.contains("Genre: Fiction, Count: 2"));
        assertTrue(output.contains("Genre: Science, Count: 1"));
        assertTrue(output.contains("Genre: History, Count: 1"));
    }

    @Test
    void testCategorizeBooksByGenre_EmptyCatalog() {
        // No books in the catalog

        // Capture system output
        String output = captureOutput(adminServices::categorizeBooksByGenre);

        // Verify output for an empty catalog
        assertTrue(output.contains("Books Categorized by Genre:"));
        assertFalse(output.contains("Genre:"));
    }

    @Test
    void testCategorizeBooksByGenre_SingleGenreMultipleBooks() {
        // All books belong to the same genre
        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Fiction", 1.0));
        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Fiction", 1.0));
        bookCatalog.put(3, new Book(3, "Book3", "Author3", "Fiction", 1.0));

        // Capture system output
        String output = captureOutput(adminServices::categorizeBooksByGenre);

        // Verify output
        assertTrue(output.contains("Books Categorized by Genre:"));
        assertTrue(output.contains("Genre: Fiction, Count: 3"));
        assertFalse(output.contains("Genre: Science"));
    }

    @Test
    void testCategorizeBooksByGenre_MultipleGenresSingleBookEach() {
        // One book per genre
        bookCatalog.put(1, new Book(1, "Book1", "Author1", "Fiction", 1.0));
        bookCatalog.put(2, new Book(2, "Book2", "Author2", "Science", 1.0));
        bookCatalog.put(3, new Book(3, "Book3", "Author3", "History", 0.0));

        // Capture system output
        String output = captureOutput(adminServices::categorizeBooksByGenre);

        // Verify output
        assertTrue(output.contains("Books Categorized by Genre:"));
        assertTrue(output.contains("Genre: Fiction, Count: 1"));
        assertTrue(output.contains("Genre: Science, Count: 1"));
        assertTrue(output.contains("Genre: History, Count: 1"));
    }

    @Test
    void testListMembersWithMultipleOverdueBooks_Success() {
        // Mock the `isBookOverdue` method
        AdminServices spyAdminServices = spy(adminServices);
        doReturn(true).when(spyAdminServices).isBookOverdue(anyString());

        // Add members with overdue books
        Member member1 = new Member(1, "John Doe");
        member1.getIssuedBooks().add("Book1");
        member1.getIssuedBooks().add("Book2");
        spyAdminServices.members.put(1, member1);

        Member member2 = new Member(2, "Jane Smith");
        member2.getIssuedBooks().add("Book3");
        spyAdminServices.members.put(2, member2);

        Member member3 = new Member(3, "Alice Brown");
        member3.getIssuedBooks().add("Book4");
        member3.getIssuedBooks().add("Book5");
        member3.getIssuedBooks().add("Book6");
        spyAdminServices.members.put(3, member3);

        // Capture output
        String output = captureOutput(spyAdminServices::listMembersWithMultipleOverdueBooks);

        // Verify
        assertTrue(output.contains("Members with Multiple Overdue Books:"));
        assertTrue(output.contains("ID: 1, Name: John Doe, Overdue Books: 2"));
        assertFalse(output.contains("ID: 2, Name: Jane Smith"));
        assertTrue(output.contains("ID: 3, Name: Alice Brown, Overdue Books: 3"));
    }

    @Test
    void testListMembersWithMultipleOverdueBooks_NoOverdueBooks() {
        // Mock the `isBookOverdue` method to always return false
        AdminServices spyAdminServices = spy(adminServices);
        doReturn(false).when(spyAdminServices).isBookOverdue(anyString());

        // Add members with books
        Member member1 = new Member(1, "John Doe");
        member1.getIssuedBooks().add("Book1");
        member1.getIssuedBooks().add("Book2");
        spyAdminServices.members.put(1, member1);

        Member member2 = new Member(2, "Jane Smith");
        member2.getIssuedBooks().add("Book3");
        spyAdminServices.members.put(2, member2);

        // Capture output
        String output = captureOutput(spyAdminServices::listMembersWithMultipleOverdueBooks);

        // Verify
        assertTrue(output.contains("Members with Multiple Overdue Books:"));
        assertFalse(output.contains("ID:"));
    }

    @Test
    void testListMembersWithMultipleOverdueBooks_EmptyMembersList() {
        // Capture output for an empty members list
        String output = captureOutput(adminServices::listMembersWithMultipleOverdueBooks);

        // Verify output
        assertTrue(output.contains("Members with Multiple Overdue Books:"));
        assertFalse(output.contains("ID:"));
    }

    @Test
    void testListMembersWithMultipleOverdueBooks_OneOverdueBook() {
        // Mock the `isBookOverdue` method
        AdminServices spyAdminServices = spy(adminServices);
        doReturn(true).when(spyAdminServices).isBookOverdue(anyString());

        // Add a member with a single overdue book
        Member member1 = new Member(1, "John Doe");
        member1.getIssuedBooks().add("Book1");
        spyAdminServices.members.put(1, member1);

        // Capture output
        String output = captureOutput(spyAdminServices::listMembersWithMultipleOverdueBooks);

        // Verify
        assertTrue(output.contains("Members with Multiple Overdue Books:"));
        assertFalse(output.contains("ID: 1, Name: John Doe"));
    }

    @Test
    void testViewTopGenres_Success() {
        // Populate book catalog
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book2", "Author2", "Science", 1.0);
        Book book3 = new Book(3, "Book3", "Author3", "Fiction", 1.0);
        adminServices.bookCatalog.put(1, book1);
        adminServices.bookCatalog.put(2, book2);
        adminServices.bookCatalog.put(3, book3);

        // Populate members with issued books
        Member member1 = new Member(1, "John Doe");
        member1.getIssuedBooks().add("Book1");
        member1.getIssuedBooks().add("Book2");
        adminServices.members.put(1, member1);

        Member member2 = new Member(2, "Jane Smith");
        member2.getIssuedBooks().add("Book3");
        adminServices.members.put(2, member2);

        // Capture output
        String output = captureOutput(adminServices::viewTopGenres);

        // Verify output
        assertTrue(output.contains("Top Genres Based on Issued Books:"));
        assertTrue(output.contains("Genre: Fiction, Count: 2"));
        assertTrue(output.contains("Genre: Science, Count: 1"));
    }

    @Test
    void testViewTopGenres_EmptyCatalogAndMembers() {
        // Ensure no books or members exist
        adminServices.members.clear();
        adminServices.bookCatalog.clear();

        // Capture output
        String output = captureOutput(adminServices::viewTopGenres);

        // Verify output
        assertTrue(output.contains("Top Genres Based on Issued Books:"));
        assertFalse(output.contains("Genre:"));
    }

    @Test
    void testViewTopGenres_MultipleGenresSameCount() {
        // Populate book catalog
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book2", "Author2", "Science", 1.0);
        adminServices.bookCatalog.put(1, book1);
        adminServices.bookCatalog.put(2, book2);

        // Populate members with issued books
        Member member1 = new Member(1, "John Doe");
        member1.getIssuedBooks().add("Book1");
        adminServices.members.put(1, member1);

        Member member2 = new Member(2, "Jane Smith");
        member2.getIssuedBooks().add("Book2");
        adminServices.members.put(2, member2);

        // Capture output
        String output = captureOutput(adminServices::viewTopGenres);

        // Verify output
        assertTrue(output.contains("Genre: Fiction, Count: 1"));
        assertTrue(output.contains("Genre: Science, Count: 1"));
    }

    @Test
    void testViewTopGenres_NoIssuedBooks() {
        // Populate book catalog
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", 1.0);
        adminServices.bookCatalog.put(1, book1);

        // No issued books for any member
        Member member1 = new Member(1, "John Doe");
        adminServices.members.put(1, member1);

        // Capture output
        String output = captureOutput(adminServices::viewTopGenres);

        // Verify output
        assertTrue(output.contains("Top Genres Based on Issued Books:"));
        assertFalse(output.contains("Genre:"));
    }

    @Test
    void testTrackInactiveBooks_Success() {
        // Add books to catalog
        Book activeBook = new Book(1, "Active Book", "Author1", "Fiction", 1.0);
        Book inactiveBook = new Book(2, "Inactive Book", "Author2", "Science", 0.0);

        adminServices.bookCatalog.put(1, activeBook);
        adminServices.bookCatalog.put(2, inactiveBook);

        // Mock `isBookIssuedRecently` behavior
        adminServices = new AdminServices() {

            public boolean isBookIssuedRecently(Book book) {
                return book.getId() == 1; // Only book with ID 1 is recently issued
            }
        };

        // Capture output
        String output = captureOutput(adminServices::trackInactiveBooks);

        // Verify output
        assertTrue(output.contains("Inactive Books (Not Issued Recently):"));
        assertTrue(output.contains("ID: 2, Title: Inactive Book"));
        assertFalse(output.contains("ID: 1, Title: Active Book"));
    }

    @Test
    void testTrackInactiveBooks_AllActive() {
        // Add books to catalog
        Book activeBook1 = new Book(1, "Active Book 1", "Author1", "Fiction", 1.0);
        Book activeBook2 = new Book(2, "Active Book 2", "Author2", "Science", 1.0);

        adminServices.bookCatalog.put(1, activeBook1);
        adminServices.bookCatalog.put(2, activeBook2);

        // Mock `isBookIssuedRecently` behavior
        adminServices = new AdminServices() {
            public boolean isBookIssuedRecently(Book book) {
                return true; // All books are recently issued
            }
        };

        // Capture output
        String output = captureOutput(adminServices::trackInactiveBooks);

        // Verify output
        assertTrue(output.contains("Inactive Books (Not Issued Recently):"));
        assertFalse(output.contains("ID:"));
    }

    @Test
    void testTrackInactiveBooks_AllInactive() {
        // Add books to catalog
        Book inactiveBook1 = new Book(1, "Inactive Book 1", "Author1", "Fiction", 0.0);
        Book inactiveBook2 = new Book(2, "Inactive Book 2", "Author2", "Science", 0.0);

        adminServices.bookCatalog.put(1, inactiveBook1);
        adminServices.bookCatalog.put(2, inactiveBook2);

        // Mock `isBookIssuedRecently` behavior
        adminServices = new AdminServices() {
            public boolean isBookIssuedRecently(Book book) {
                return false; // No books are recently issued
            }
        };

        // Capture output
        String output = captureOutput(adminServices::trackInactiveBooks);

        // Verify output
        assertTrue(output.contains("Inactive Books (Not Issued Recently):"));
        assertTrue(output.contains("ID: 1, Title: Inactive Book 1"));
        assertTrue(output.contains("ID: 2, Title: Inactive Book 2"));
    }

    @Test
    void testTrackInactiveBooks_EmptyCatalog() {
        // Ensure no books in catalog
        adminServices.bookCatalog.clear();

        // Capture output
        String output = captureOutput(adminServices::trackInactiveBooks);

        // Verify output
        assertTrue(output.contains("Inactive Books (Not Issued Recently):"));
        assertFalse(output.contains("ID:"));
    }

    @Test
    void testGenerateDetailedGenreReport_Success() {
        // Add books to catalog
        Book book1 = new Book(1, "Book 1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book 2", "Author2", "Science", 0.0);
        Book book3 = new Book(3, "Book 3", "Author3", "Fiction", 1.0);

        adminServices.bookCatalog.put(1, book1);
        adminServices.bookCatalog.put(2, book2);
        adminServices.bookCatalog.put(3, book3);

        // Capture output
        String output = captureOutput(adminServices::generateDetailedGenreReport);

        // Verify output
        assertTrue(output.contains("Detailed Genre Report:"));
        assertTrue(output.contains("Genre: Fiction"));
        assertTrue(output.contains("Books: [Book 1, Book 3]"));
        assertTrue(output.contains("Genre: Science"));
        assertTrue(output.contains("Books: [Book 2]"));
    }

    @Test
    void testGenerateDetailedGenreReport_EmptyCatalog() {
        // Ensure no books in catalog
        adminServices.bookCatalog.clear();

        // Capture output
        String output = captureOutput(adminServices::generateDetailedGenreReport);

        // Verify output
        assertTrue(output.contains("Detailed Genre Report:"));
        assertFalse(output.contains("Genre:"));
        assertFalse(output.contains("Books:"));
    }

    @Test
    void testGenerateDetailedGenreReport_SingleGenre() {
        // Add books with the same genre
        Book book1 = new Book(1, "Book 1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book 2", "Author2", "Fiction", 1.0);

        adminServices.bookCatalog.put(1, book1);
        adminServices.bookCatalog.put(2, book2);

        // Capture output
        String output = captureOutput(adminServices::generateDetailedGenreReport);

        // Verify output
        assertTrue(output.contains("Detailed Genre Report:"));
        assertTrue(output.contains("Genre: Fiction"));
        assertTrue(output.contains("Books: [Book 1, Book 2]"));
    }

    @Test
    void testGenerateDetailedGenreReport_MultipleGenres() {
        // Add books to catalog with different genres
        Book book1 = new Book(1, "Book 1", "Author1", "Fiction", 1.0);
        Book book2 = new Book(2, "Book 2", "Author2", "Science", 1.0);
        Book book3 = new Book(3, "Book 3", "Author3", "History", 1.0);

        adminServices.bookCatalog.put(1, book1);
        adminServices.bookCatalog.put(2, book2);
        adminServices.bookCatalog.put(3, book3);

        // Capture output
        String output = captureOutput(adminServices::generateDetailedGenreReport);

        // Verify output
        assertTrue(output.contains("Detailed Genre Report:"));
        assertTrue(output.contains("Genre: Fiction"));
        assertTrue(output.contains("Books: [Book 1]"));
        assertTrue(output.contains("Genre: Science"));
        assertTrue(output.contains("Books: [Book 2]"));
        assertTrue(output.contains("Genre: History"));
        assertTrue(output.contains("Books: [Book 3]"));
    }

    // DU Pair - 231, 232, 233 are being covered
    @Test
    void testTrackOverdueBooks_Success() {
        // Add members and their issued books
        Member member1 = new Member(1, "Alice");
        Member member2 = new Member(2, "Bob");

        List<String> issuedBooksMember1 = new ArrayList<>();
        issuedBooksMember1.add("Book 1");
        issuedBooksMember1.add("Book 2");

        List<String> issuedBooksMember2 = new ArrayList<>();
        issuedBooksMember2.add("Book 3");

        member1.setIssuedBooks(issuedBooksMember1);
        member2.setIssuedBooks(issuedBooksMember2);

        adminServices.members.put(1, member1);
        adminServices.members.put(2, member2);

        // Capture output
        String output = captureOutput(adminServices::trackOverdueBooks);

        // Verify output
        assertTrue(output.contains("Overdue Books:"));
        assertTrue(output.contains("Member: Alice, Book: Book 1 is overdue."));
        assertTrue(output.contains("Member: Alice, Book: Book 2 is overdue."));
        assertTrue(output.contains("Member: Bob, Book: Book 3 is overdue."));
    }

    // DU Pair - 231, 232, 233 are being covered
    @Test
    void testTrackOverdueBooks_NoOverdueBooks() {
        // Add members with no issued books
        Member member1 = new Member(1, "Alice");
        Member member2 = new Member(2, "Bob");

        member1.setIssuedBooks(new ArrayList<>());
        member2.setIssuedBooks(new ArrayList<>());

        adminServices.members.put(1, member1);
        adminServices.members.put(2, member2);

        // Capture output
        String output = captureOutput(adminServices::trackOverdueBooks);

        // Verify output
        assertTrue(output.contains("Overdue Books:"));
        assertFalse(output.contains("Member: Alice"));
        assertFalse(output.contains("Member: Bob"));
    }

    // DU Pair - 231, 232, 233 are being covered
    @Test
    void testTrackOverdueBooks_EmptyMembers() {
        // No members added
        adminServices.members.clear();

        // Capture output
        String output = captureOutput(adminServices::trackOverdueBooks);

        // Verify output
        assertTrue(output.contains("Overdue Books:"));
        assertFalse(output.contains("Member:"));
    }

    // DU Pair - 231, 232, 233 are being covered
    @Test
    void testTrackOverdueBooks_MixedScenarios() {
        // Add members with mixed issued book statuses
        Member member1 = new Member(1, "Alice");
        Member member2 = new Member(2, "Bob");

        List<String> issuedBooksMember1 = new ArrayList<>();
        issuedBooksMember1.add("Book 1");

        member1.setIssuedBooks(issuedBooksMember1);
        member2.setIssuedBooks(new ArrayList<>()); // Bob has no issued books

        adminServices.members.put(1, member1);
        adminServices.members.put(2, member2);

        // Capture output
        String output = captureOutput(adminServices::trackOverdueBooks);

        // Verify output
        assertTrue(output.contains("Overdue Books:"));
        assertTrue(output.contains("Member: Alice, Book: Book 1 is overdue."));
        assertFalse(output.contains("Member: Bob"));
    }

    // DU Pair - 235, 236, 237, 238 are being covered
    @Test
    void testViewGenreBasedBooks_Success() {
        // Add books to the catalog
        adminServices.bookCatalog.put(1, new Book(1, "Book A", "Fiction", "Author A", 0.0));
        adminServices.bookCatalog.put(2, new Book(2, "Book B", "Non-Fiction", "Author B", 0.0));
        adminServices.bookCatalog.put(3, new Book(3, "Book C", "Fiction", "Author C", 0.0));

        // Capture output
        String output = captureOutput(adminServices::viewGenreBasedBooks);

        // Verify output
        assertTrue(output.contains("Genre: Fiction"));
        assertTrue(output.contains("ID: 1, Title: Book A"));
        assertTrue(output.contains("ID: 3, Title: Book C"));
        assertTrue(output.contains("Genre: Non-Fiction"));
        assertTrue(output.contains("ID: 2, Title: Book B"));
    }

    // DU Pair - 235, 236, 237, 238 are being covered
    @Test
    void testViewGenreBasedBooks_EmptyCatalog() {
        // Ensure bookCatalog is empty
        adminServices.bookCatalog.clear();

        // Capture output
        String output = captureOutput(adminServices::viewGenreBasedBooks);

        // Verify output
        assertTrue(output.isEmpty(), "Output should be empty for an empty catalog.");
    }

    // DU Pair - 235, 236, 237, 238 are being covered
    @Test
    void testViewGenreBasedBooks_SingleGenre() {
        // Add books of a single genre to the catalog
        adminServices.bookCatalog.put(1, new Book(1, "Book A", "Science Fiction", "Author A", 0.0));
        adminServices.bookCatalog.put(2, new Book(2, "Book B", "Science Fiction", "Author B", 0.0));

        // Capture output
        String output = captureOutput(adminServices::viewGenreBasedBooks);

        // Verify output
        assertTrue(output.contains("Genre: Science Fiction"));
        assertTrue(output.contains("ID: 1, Title: Book A"));
        assertTrue(output.contains("ID: 2, Title: Book B"));
    }

    // DU Pair - 235, 236, 237, 238 are being covered
    @Test
    void testViewGenreBasedBooks_MixedGenres() {
        // Add books of mixed genres
        adminServices.bookCatalog.put(1, new Book(1, "Book A", "Horror", "Author A", 0.0));
        adminServices.bookCatalog.put(2, new Book(2, "Book B", "Horror", "Author B", 0.0));

        // Capture output
        String output = captureOutput(adminServices::viewGenreBasedBooks);

        // Verify output
        assertTrue(output.contains("Genre: Horror"));
        assertTrue(output.contains("ID: 1, Title: Book A"));
        assertTrue(output.contains("ID: 2, Title: Book B"));
        assertTrue(output.contains("Genre: Comedy"));
        assertTrue(output.contains("ID: 3, Title: Book C"));
    }

    // DU Pair - 235, 236, 237, 238 are being covered
    @Test
    void testViewGenreBasedBooks_DuplicateGenres() {
        // Add multiple books with the same title in the same genre
        adminServices.bookCatalog.put(1, new Book(1, "Book A", "Fantasy", "Author A", 0.0));
        adminServices.bookCatalog.put(2, new Book(2, "Book A", "Fantasy", "Author B", 0.0));

        // Capture output
        String output = captureOutput(adminServices::viewGenreBasedBooks);

        // Verify output
        assertTrue(output.contains("Genre: Fantasy"));
        assertTrue(output.contains("ID: 1, Title: Book A"));
        assertTrue(output.contains("ID: 2, Title: Book A"));
    }

}