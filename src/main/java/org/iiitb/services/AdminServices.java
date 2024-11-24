package org.iiitb.services;

import org.iiitb.database.DatabaseServices;
import org.iiitb.entities.Book;
import org.iiitb.entities.Member;

import java.sql.Date;
import java.util.*;

public class AdminServices {

    Scanner scanner = new Scanner(System.in);
    Map<Integer, Book> bookCatalog;
    Map<Integer, Member> members = new HashMap<>();

    private MemberServices memberServices = new MemberServices();
    private DatabaseServices databaseServices;

    public AdminServices(){
        databaseServices = new DatabaseServices();
        bookCatalog = new HashMap<>();
        refreshBookCatalog();

    }

    private void refreshBookCatalog(){
        bookCatalog = new HashMap<>();
        List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
        allBooksFromDatabase.forEach(book -> {
            bookCatalog.put(book.getId(), book);
        });
    }

    private double checkFineForMember(int memberId) {
        Member member = members.get(memberId);
        double totalFine = 0;
        if (member != null) {
            for (String bookTitle : member.getIssuedBooks()) {
                // Simulate fine calculation logic
                totalFine += 2.5; // Assuming $2.5 fine per book
            }
        }
        return totalFine;
    }

    ///////////////////////////To-do
    private boolean isBookIssuedRecently(Book book) {
        // Placeholder for recent issuance logic
        return Math.random() > 0.7; // Randomly simulate recent issuance
    }

    public void addBook(Book book) {
        try {
            if (bookCatalog.containsKey(book.getId())) {
                System.out.println("AS: Error: A book with this ID already exists.");
            } else if (book.getTitle().isEmpty() || book.getAuthor().isEmpty() || book.getGenre().isEmpty()) {
                System.out.println("AS: Error: All fields are required!");
            } else {
                databaseServices.addBookToDatabase(book);
                refreshBookCatalog();
                System.out.println("AS: Book added successfully!");
            }
        } catch (Exception e) {
            System.out.println("AS: Error adding book: " + e.getMessage());
        }
    }

    public void deleteBook(int id) {
        try {
            if (bookCatalog.containsKey(id)) {
                Book book = bookCatalog.get(id);

                if (book.getIsIssued()) {
                    System.out.println("Error: Cannot delete a book that is currently issued.");
                } else {
                    bookCatalog.remove(id);
                    System.out.println("Book deleted successfully!");
                }
            } else {
                System.out.println("Error: Book not found.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    public void viewAllBooks() {
        try {
            if (bookCatalog.isEmpty()) {
                System.out.println("No books available in the catalog.");
            } else {
                for (Book book : bookCatalog.values()) {
                    System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() +
                            ", Genre: " + book.getGenre() + ", Price: $" + book.getPrice() + ", Status: " + (book.getIsIssued() ? "Issued" : "Available"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing books: " + e.getMessage());
        }
    }

    public void calculateTotalBooks() {
        try {
            int totalBooks = bookCatalog.size();
            System.out.println("Total books in the catalog: " + totalBooks);
        } catch (Exception e) {
            System.out.println("Error calculating total books: " + e.getMessage());
        }
    }

    public void calculateTotalLibraryValue() {
        try {
            double totalValue = 0;
            for (Book book : bookCatalog.values()) {
                totalValue += book.getPrice();
            }
            System.out.println("Total library value: $" + totalValue);
        } catch (Exception e) {
            System.out.println("Error calculating total library value: " + e.getMessage());
        }
    }

    public void calculateTotalIssuedBooks() {
        try {
            int totalIssued = 0;
            for (Book book : bookCatalog.values()) {
                if (book.getIsIssued()) {
                    totalIssued++;
                }
            }
            System.out.println("Total books issued: " + totalIssued);
        } catch (Exception e) {
            System.out.println("Error calculating total issued books: " + e.getMessage());
        }
    }

    public void calculateMostPopularBook() {
        try {
            Book mostPopularBook = null;
            int maxIssues = 0;

            for (Book book : bookCatalog.values()) {
                int issuedCount = 0;
                for (Member member : members.values()) {
                    if (member.getIssuedBooks().contains(book.getTitle())) {
                        issuedCount++;
                    }
                }

                if (issuedCount > maxIssues) {
                    mostPopularBook = book;
                    maxIssues = issuedCount;
                }
            }

            if (mostPopularBook != null) {
                System.out.println("Most popular book: " + mostPopularBook.getTitle() + " with " + maxIssues + " issues.");
            } else {
                System.out.println("No books have been issued yet.");
            }
        } catch (Exception e) {
            System.out.println("Error calculating most popular book: " + e.getMessage());
        }
    }

    public void calculateTotalFines() {
        try {
            double fineAmountPerDay = 1.0;  // Fine per day for overdue books
            double totalFines = 0;

            // Assuming a method to check overdue books (not implemented here)
            for (Member member : members.values()) {
                for (String bookTitle : member.getIssuedBooks()) {
                    // Assuming a logic to calculate overdue days (dummy value here)
                    int overdueDays = 5;  // Placeholder for actual overdue days calculation

                    if (overdueDays > 0) {
                        totalFines += fineAmountPerDay * overdueDays;
                    }
                }
            }

            System.out.println("Total fines collected: $" + totalFines);
        } catch (Exception e) {
            System.out.println("Error calculating total fines: " + e.getMessage());
        }
    }

    public void getBookDetailsById(int id) {
        Book book = bookCatalog.get(id);
        if (book != null) {
            System.out.println("Book Details: " + book.getTitle() + " by " + book.getAuthor() + ", Genre: " + book.getGenre() + ", Status: " + (book.getIsIssued() ? "Issued" : "Available"));
        } else {
            System.out.println("Book not found.");
        }
    }

    public void findBooksByGenre(String genre) {
        boolean found = false;
        for (Book book : bookCatalog.values()) {
            if (book.getGenre().equalsIgnoreCase(genre)) {
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found for the given genre.");
        }
    }

    public void getBooksIssuedByMember(int memberId) {
        Member member = members.get(memberId);
        if (member != null && !member.getIssuedBooks().isEmpty()) {
            System.out.println("Books issued by " + member.getName() + ":");
            for (String bookTitle : member.getIssuedBooks()) {
                System.out.println(bookTitle);
            }
        } else {
            System.out.println("No books issued by this member.");
        }
    }

    public boolean checkBookAvailability(int bookId) {
        Book book = bookCatalog.get(bookId);
        return book != null && !book.getIsIssued();
    }

    public boolean extendBookReturnDate(int bookId, int memberId) {
        Book book = bookCatalog.get(bookId);
        Member member = members.get(memberId);
        if (book != null && member != null && member.getIssuedBooks().contains(book.getTitle())) {
            // Logic to extend return date (if allowed)
            System.out.println("Book return date extended for: " + book.getTitle());
            return true;
        }
        System.out.println("Unable to extend return date. Book not issued by this member.");
        return false;
    }

    public int calculateTotalBooksIssuedByGenre(String genre) {
        int count = 0;
        for (Book book : bookCatalog.values()) {
            if (book.getGenre().equalsIgnoreCase(genre) && book.getIsIssued()) {
                count++;
            }
        }
        return count;
    }

    public void updateBookPrice(int bookId, double newPrice) {
        Book book = bookCatalog.get(bookId);
        if (book != null) {
            // Assuming each book has a price field
            book.setPrice(newPrice);
            System.out.println("Price updated for: " + book.getTitle());
        } else {
            System.out.println("Book not found.");
        }
    }

    public void findBooksByAuthor(String authorName) {
        boolean found = false;
        for (Book book : bookCatalog.values()) {
            if (book.getAuthor().equalsIgnoreCase(authorName)) {
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found by this author.");
        }
    }

    public Member findMostIssuedMember() {
        Member mostIssuedMember = null;
        int maxBooksIssued = 0;
        for (Member member : members.values()) {
            if (member.getIssuedBooks().size() > maxBooksIssued) {
                mostIssuedMember = member;
                maxBooksIssued = member.getIssuedBooks().size();
            }
        }
        return mostIssuedMember;
    }

    public double calculateBooksIssuedByMemberPercentage(int memberId) {
        Member member = members.get(memberId);
        int totalBooks = bookCatalog.size();
        int issuedBooks = member != null ? member.getIssuedBooks().size() : 0;
        return totalBooks == 0 ? 0 : (issuedBooks / (double) totalBooks) * 100;
    }

    public void generateMemberReport(int memberId) {
        Member member = members.get(memberId);
        if (member != null) {
            System.out.println("Member Report:");
            System.out.println("Name: " + member.getName());
            System.out.println("Issued Books: " + (member.getIssuedBooks().isEmpty() ? "None" : String.join(", ", member.getIssuedBooks())));
            double fine = checkFineForMember(memberId);
            System.out.println("Total Fine: $" + fine);
        } else {
            System.out.println("No member found with the given ID.");
        }
    }

    public void compareMemberActivity(int memberId1, int memberId2) {
        Member member1 = members.get(memberId1);
        Member member2 = members.get(memberId2);
        if (member1 != null && member2 != null) {
            System.out.println("Comparison of Members:");
            System.out.println(member1.getName() + " - Books Issued: " + member1.getIssuedBooks().size());
            System.out.println(member2.getName() + " - Books Issued: " + member2.getIssuedBooks().size());
            double fine1 = checkFineForMember(memberId1);
            double fine2 = checkFineForMember(memberId2);
            System.out.println(member1.getName() + " - Total Fine: $" + fine1);
            System.out.println(member2.getName() + " - Total Fine: $" + fine2);
        } else {
            System.out.println("One or both members not found.");
        }
    }

    public void recommendBooks(int memberId) {
        Member member = members.get(memberId);
        if (member != null && !member.getIssuedBooks().isEmpty()) {
            Set<String> genres = new HashSet<>();
            for (String bookTitle : member.getIssuedBooks()) {
                for (Book book : bookCatalog.values()) {
                    if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                        genres.add(book.getGenre());
                    }
                }
            }
            System.out.println("Recommended Books Based on Your Interest:");
            for (Book book : bookCatalog.values()) {
                if (!book.getIsIssued() && genres.contains(book.getGenre())) {
                    System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                }
            }
        } else {
            System.out.println("No recommendations available. Start issuing books to get recommendations!");
        }
    }

    public Book trackMostPopularBook() {
        Map<String, Integer> bookFrequency = new HashMap<>();
        for (Member member : members.values()) {
            for (String bookTitle : member.getIssuedBooks()) {
                bookFrequency.put(bookTitle, bookFrequency.getOrDefault(bookTitle, 0) + 1);
            }
        }
        String mostPopularBookTitle = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : bookFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostPopularBookTitle = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        for (Book book : bookCatalog.values()) {
            if (book.getTitle().equalsIgnoreCase(mostPopularBookTitle)) {
                System.out.println("Most Popular Book: " + book.getTitle() + " by " + book.getAuthor());
                return book;
            }
        }
        System.out.println("No popular book found.");
        return null;
    }

    public void archiveOldBooks() {
        List<Integer> booksToArchive = new ArrayList<>();
        for (Book book : bookCatalog.values()) {
            // Simulate condition to archive
            if (!book.getIsIssued()) {
                booksToArchive.add(book.getId());
            }
        }
        for (int bookId : booksToArchive) {
            bookCatalog.remove(bookId);
            System.out.println("Book ID " + bookId + " archived.");
        }
        System.out.println("Archival complete.");
    }

    public void generateLibraryStatistics() {
        int totalMembers = members.size();
        int totalBooks = bookCatalog.size();
        int issuedBooksCount = 0;
        double totalFines = 0;

        for (Member member : members.values()) {
            issuedBooksCount += member.getIssuedBooks().size();
            totalFines += checkFineForMember(member.getId());
        }

        System.out.println("Library Statistics:");
        System.out.println("Total Members: " + totalMembers);
        System.out.println("Total Books: " + totalBooks);
        System.out.println("Total Issued Books: " + issuedBooksCount);
        System.out.println("Average Books Issued per Member: " + (totalMembers > 0 ? (issuedBooksCount / totalMembers) : 0));
        System.out.println("Total Fines Collected: $" + totalFines);
    }

    public void trackBooksIssuedInDateRange(Date startDate, Date endDate) {
        System.out.println("Books issued between " + startDate + " and " + endDate + ":");
        // Simulate tracking with dummy data
        for (Book book : bookCatalog.values()) {
            // Assume `book.lastIssuedDate` exists and compare with startDate/endDate
            System.out.println("Book ID: " + book.getId() + ", Title: " + book.getTitle());
        }
    }

    public void calculateGenrePopularity() {
        Map<String, Integer> genrePopularity = new HashMap<>();
        for (Book book : bookCatalog.values()) {
            if (book.getIsIssued()) {
                genrePopularity.put(book.getGenre(), genrePopularity.getOrDefault(book.getGenre(), 0) + 1);
            }
        }
        System.out.println("Genre Popularity:");
        for (Map.Entry<String, Integer> entry : genrePopularity.entrySet()) {
            System.out.println("Genre: " + entry.getKey() + ", Issued Count: " + entry.getValue());
        }
    }

    public void trackOverdueBooks() {
        System.out.println("Overdue Books:");
        for (Member member : members.values()) {
            for (String bookTitle : member.getIssuedBooks()) {
                // Assume a `dueDate` for issued books (this could be simulated or static)
                System.out.println("Member: " + member.getName() + ", Book: " + bookTitle + " is overdue.");
            }
        }
    }

    public void viewGenreBasedBooks() {
        Map<String, List<Book>> genreBooks = new HashMap<>();
        for (Book book : bookCatalog.values()) {
            genreBooks.putIfAbsent(book.getGenre(), new ArrayList<>());
            genreBooks.get(book.getGenre()).add(book);
        }
        for (Map.Entry<String, List<Book>> entry : genreBooks.entrySet()) {
            System.out.println("Genre: " + entry.getKey());
            for (Book book : entry.getValue()) {
                System.out.println("  ID: " + book.getId() + ", Title: " + book.getTitle());
            }
        }
    }

    public void calculateAverageBorrowTime() {
        double totalBorrowTime = 0;
        int borrowCount = 0;
        for (Member member : members.values()) {
            borrowCount += member.getIssuedBooks().size();
            // Simulate total borrow time (dummy data or logic)
            totalBorrowTime += member.getIssuedBooks().size() * 7; // Assume 7 days per book for simplicity
        }
        System.out.println("Average Borrow Time: " + (borrowCount > 0 ? totalBorrowTime / borrowCount : 0) + " days");
    }

    public void suggestBooksToRestock() {
        Map<String, Integer> bookDemand = new HashMap<>();
        for (Member member : members.values()) {
            for (String bookTitle : member.getIssuedBooks()) {
                bookDemand.put(bookTitle, bookDemand.getOrDefault(bookTitle, 0) + 1);
            }
        }
        System.out.println("Books to Restock:");
        for (Book book : bookCatalog.values()) {
            if (bookDemand.getOrDefault(book.getTitle(), 0) > 5) { // Restock threshold
                System.out.println("Title: " + book.getTitle() + ", Demand: " + bookDemand.get(book.getTitle()));
            }
        }
    }

    public void findInactiveMembers() {
        System.out.println("Inactive Members:");
        for (Member member : members.values()) {
            if (member.getIssuedBooks().isEmpty()) {
                System.out.println("ID: " + member.getId() + ", Name: " + member.getName());
            }
        }
    }

    public void viewBookIssueHistory(int bookId) {
        System.out.println("Issue History for Book ID: " + bookId);
        for (Member member : members.values()) {
            if (member.getIssuedBooks().contains(bookCatalog.get(bookId).getTitle())) {
                System.out.println("  Issued by Member ID: " + member.getId() + ", Name: " + member.getName());
            }
        }
    }

    public void predictBookDemand() {
        Map<String, Integer> demandPrediction = new HashMap<>();
        for (Member member : members.values()) {
            for (String bookTitle : member.getIssuedBooks()) {
                demandPrediction.put(bookTitle, demandPrediction.getOrDefault(bookTitle, 0) + 1);
            }
        }
        System.out.println("Predicted Book Demand:");
        for (Map.Entry<String, Integer> entry : demandPrediction.entrySet()) {
            System.out.println("Title: " + entry.getKey() + ", Predicted Demand: " + entry.getValue() * 1.2); // Increase by 20% for future prediction
        }
    }

    public void addBookRatings(int memberId, int bookId, int rating) {
        if (members.containsKey(memberId) && bookCatalog.containsKey(bookId)) {
            System.out.println("Member " + members.get(memberId).getName() + " rated book " + bookCatalog.get(bookId).getTitle() + ": " + rating + "/5");
        } else {
            System.out.println("Invalid member or book ID.");
        }
    }

    /////////////////////////////////To-Do
    public void showTopRatedBooks() {
        System.out.println("Top Rated Books (Dummy Data):");
        System.out.println("1. The Great Gatsby - 4.8/5");
        System.out.println("2. To Kill a Mockingbird - 4.7/5");
        System.out.println("3. 1984 - 4.6/5");
    }

    /////////////////////////////////To-Do
    public void trackDailyTransactions(Date date) {
        System.out.println("Transactions on " + date + ":");
        System.out.println("Issued: 10 books (Dummy Data)");
        System.out.println("Returned: 5 books (Dummy Data)");
    }

    public void updateMemberDetails(int memberId, String newName) {
        if (members.containsKey(memberId)) {
            members.get(memberId).setName(newName);
            System.out.println("Member details updated successfully!");
        } else {
            System.out.println("Member not found.");
        }
    }

    public void viewIssuedBooksByGenre(String genre) {
        System.out.println("Issued Books in Genre: " + genre);
        boolean found = false;
        for (Book book : bookCatalog.values()) {
            if (book.getIsIssued() && book.getGenre().equalsIgnoreCase(genre)) {
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No issued books found in this genre.");
        }
    }

    public void calculatePenalty(Member member, int daysOverdue) {
        int penaltyRate = 5; // Assume $5 per day
        int penalty = daysOverdue * penaltyRate * member.getIssuedBooks().size();
        System.out.println("Penalty for Member " + member.getName() + ": $" + penalty);
    }

    public void recommendBooksBasedOnGenre(Member member) {
        Map<String, Integer> genreCount = new HashMap<>();
        for (String bookTitle : member.getIssuedBooks()) {
            for (Book book : bookCatalog.values()) {
                if (book.getTitle().equals(bookTitle)) {
                    genreCount.put(book.getGenre(), genreCount.getOrDefault(book.getGenre(), 0) + 1);
                }
            }
        }
        String favoriteGenre = genreCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
        System.out.println("Recommended Books in Genre: " + favoriteGenre);
        for (Book book : bookCatalog.values()) {
            if (!book.getIsIssued() && book.getGenre().equalsIgnoreCase(favoriteGenre)) {
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle());
            }
        }
    }

    public void categorizeBooksByGenre() {
        Map<String, Integer> genreCounts = new HashMap<>();
        for (Book book : bookCatalog.values()) {
            genreCounts.put(book.getGenre(), genreCounts.getOrDefault(book.getGenre(), 0) + 1);
        }
        System.out.println("Books Categorized by Genre:");
        for (Map.Entry<String, Integer> entry : genreCounts.entrySet()) {
            System.out.println("Genre: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }

    public void listMembersWithMultipleOverdueBooks() {
        System.out.println("Members with Multiple Overdue Books:");
        for (Member member : members.values()) {
            int overdueCount = 0;
            for (String bookTitle : member.getIssuedBooks()) {
                if (isBookOverdue(bookTitle)) {
                    overdueCount++;
                }
            }
            if (overdueCount > 1) {
                System.out.println("ID: " + member.getId() + ", Name: " + member.getName() + ", Overdue Books: " + overdueCount);
            }
        }
    }

    public boolean isBookOverdue(String bookTitle) {
        // Placeholder for overdue logic (e.g., issued date comparison)
        return Math.random() > 0.5; // Randomly simulate overdue
    }

    public void viewTopGenres() {
        Map<String, Integer> genrePopularity = new HashMap<>();
        for (Member member : members.values()) {
            for (String bookTitle : member.getIssuedBooks()) {
                for (Book book : bookCatalog.values()) {
                    if (book.getTitle().equals(bookTitle)) {
                        genrePopularity.put(book.getGenre(), genrePopularity.getOrDefault(book.getGenre(), 0) + 1);
                    }
                }
            }
        }
        System.out.println("Top Genres Based on Issued Books:");
        genrePopularity.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> System.out.println("Genre: " + entry.getKey() + ", Count: " + entry.getValue()));
    }

    public void trackInactiveBooks() {
        System.out.println("Inactive Books (Not Issued Recently):");
        for (Book book : bookCatalog.values()) {
            if (!isBookIssuedRecently(book)) {
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle());
            }
        }
    }

    public void generateDetailedGenreReport() {
        Map<String, List<String>> genreToBooks = new HashMap<>();
        for (Book book : bookCatalog.values()) {
            genreToBooks.putIfAbsent(book.getGenre(), new ArrayList<>());
            genreToBooks.get(book.getGenre()).add(book.getTitle());
        }
        System.out.println("Detailed Genre Report:");
        for (Map.Entry<String, List<String>> entry : genreToBooks.entrySet()) {
            System.out.println("Genre: " + entry.getKey());
            System.out.println("Books: " + entry.getValue());
        }
    }

    //////////////////////////////////////////////To-do
    public void simulateLibraryDay() {
        System.out.println("Simulating a Day in the Library...");
        for (int i = 0; i < 10; i++) {
            int randomMemberId = (int) (Math.random() * members.size()) + 1;
            if (members.containsKey(randomMemberId)) {
                Member member = members.get(randomMemberId);
                int action = (int) (Math.random() * 3) + 1;
                switch (action) {
                    /////////////////////////////////////////Correct it
                    case 1 -> memberServices.issueBook(member, 0);
                    case 2 -> memberServices.returnBook(member, 0);
                    case 3 -> recommendBooksBasedOnGenre(member);
                }
            }
        }
        System.out.println("Day Simulation Complete!");
    }

}
