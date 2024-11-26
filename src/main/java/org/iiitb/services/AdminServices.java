package org.iiitb.services;

import org.iiitb.database.DatabaseServices;
import org.iiitb.entities.Book;
import org.iiitb.entities.Member;

import java.sql.Date;
import java.util.*;

public class AdminServices {

    private DatabaseServices databaseServices;
    ////////////////////////////////////////////////////////////////////////////////////////

    public AdminServices(){
        databaseServices = new DatabaseServices();
    }

    public void addBook(Book book) {
        try {
            if (databaseServices.findBookById(book.getId()).getId() != null) {
                System.out.println("AS: Error: A book with this ID already exists.");
            } else if (book.getTitle().isEmpty() || book.getAuthor().isEmpty() || book.getGenre().isEmpty()) {
                System.out.println("AS: Error: All fields are required!");
            } else {
                databaseServices.addBookToDatabase(book);
                System.out.println("AS: Book added successfully!");
            }
        } catch (Exception e) {
            System.out.println("AS: Error adding book: " + e.getMessage());
        }
    }

    public void deleteBook(int id) {
        try {
            Book bookById = databaseServices.findBookById(id);
            if (bookById.getId() != null) {

                if (bookById.getIsIssued()) {
                    System.out.println("Error: Cannot delete a book that is currently issued.");
                } else {
                    databaseServices.deleteBookById(id);
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
            List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
            if (allBooksFromDatabase.isEmpty()) {
                System.out.println("No books available in the catalog.");
            } else {
                for (Book book : allBooksFromDatabase) {
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
            List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
            int totalBooks = allBooksFromDatabase.size();
            System.out.println("Total books in the catalog: " + totalBooks);
        } catch (Exception e) {
            System.out.println("Error calculating total books: " + e.getMessage());
        }
    }

    public void calculateTotalLibraryValue() {
        try {
            List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
            double totalValue = 0;
            for (Book book : allBooksFromDatabase) {
                totalValue += book.getPrice();
            }
            System.out.println("Total library value: $" + totalValue);
        } catch (Exception e) {
            System.out.println("Error calculating total library value: " + e.getMessage());
        }
    }

    public void calculateTotalIssuedBooks() {
        try {
            List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
            int totalIssued = 0;
            for (Book book : allBooksFromDatabase) {
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
            List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
            List<Member> allMembersFromDatabase = databaseServices.getAllMembersFromDatabase();

            Book mostPopularBook = null;
            int maxIssues = 0;

            for (Book book : allBooksFromDatabase) {
                int issuedCount = 0;
                for (Member member : allMembersFromDatabase) {
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

    public void getBookDetailsById(int id) {
        Book book = databaseServices.findBookById(id);
        if (book != null) {
            System.out.println("Book Details: " + book.getTitle() + " by " + book.getAuthor() + ", Genre: " + book.getGenre() + ", Status: " + (book.getIsIssued() ? "Issued" : "Available"));
        } else {
            System.out.println("Book not found.");
        }
    }

    public void findBooksByGenre(String genre) {
        boolean found = false;
        List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
        for (Book book : allBooksFromDatabase) {
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
        Member member = databaseServices.findMemberById(memberId);
        if (member != null) {
            List<Book> booksByMemberId = databaseServices.findBooksByMemberId(memberId);
            if(booksByMemberId.isEmpty()){
                System.out.println("No books issued by this member");
            }else {
                System.out.println("Books issued by " + member.getName() + ":");
                for (Book book : member.getIssuedBooks()) {
                    System.out.println(book.getTitle());
            }
            }
        } else {
            System.out.println("There is no member with this member id");
        }
    }

    public boolean checkBookAvailability(int bookId) {
        Book book = databaseServices.findBookById(bookId);
        return book != null && !book.getIsIssued();
    }

    public int calculateTotalBooksIssuedByGenre(String genre) {
        int count = 0;
        List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
        for (Book book : allBooksFromDatabase) {
            if (book.getGenre().equalsIgnoreCase(genre) && book.getIsIssued()) {
                count++;
            }
        }
        return count;
    }

    public void updateBookPrice(int bookId, double newPrice) {
        Book book = databaseServices.findBookById(bookId);
        if (book != null) {
            boolean b = databaseServices.updateBookPrice(book.getId(), newPrice);
            if(b){
                System.out.println("Price updated for: " + book.getTitle());
            }else{
                System.out.println("Price couldn't be updated");
            }
        } else {
            System.out.println("Book not found.");
        }
    }

    public void findBooksByAuthor(String authorName) {
        boolean found = false;
        List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
        for (Book book : allBooksFromDatabase) {
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
        List<Member> allMembersFromDatabase = databaseServices.getAllMembersFromDatabase();
        for (Member member : allMembersFromDatabase) {
            if (member.getIssuedBooks().size() > maxBooksIssued) {
                mostIssuedMember = member;
                maxBooksIssued = member.getIssuedBooks().size();
            }
        }
        return mostIssuedMember;
    }

    public void calculateGenrePopularity() {
        Map<String, Integer> genrePopularity = new HashMap<>();
        List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
        for (Book book : allBooksFromDatabase) {
            if (book.getIsIssued()) {
                genrePopularity.put(book.getGenre(), genrePopularity.getOrDefault(book.getGenre(), 0) + 1);
            }
        }
        System.out.println("Genre Popularity:");
        for (Map.Entry<String, Integer> entry : genrePopularity.entrySet()) {
            System.out.println("Genre: " + entry.getKey() + ", Issued Count: " + entry.getValue());
        }
    }

    public void findInactiveMembers() {
        System.out.println("Inactive Members:");
        List<Member> allMembersFromDatabase = databaseServices.getAllMembersFromDatabase();
        for (Member member : allMembersFromDatabase) {
            if (member.getIssuedBooks().isEmpty()) {
                System.out.println("ID: " + member.getId() + ", Name: " + member.getName());
            }
        }
    }

    public void updateMemberDetails(int memberId, String newName) {
        Member memberById = databaseServices.findMemberById(memberId);
        if (memberById != null) {
            databaseServices.updateMemberName(memberId, newName);
            System.out.println("Member details updated successfully!");
        } else {
            System.out.println("Member not found.");
        }
    }
    public void categorizeBooksByGenre() {
        Map<String, Integer> genreCounts = new HashMap<>();
        List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
        for (Book book : allBooksFromDatabase) {
            genreCounts.put(book.getGenre(), genreCounts.getOrDefault(book.getGenre(), 0) + 1);
        }
        System.out.println("Books Categorized by Genre:");
        for (Map.Entry<String, Integer> entry : genreCounts.entrySet()) {
            System.out.println("Genre: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }
}
