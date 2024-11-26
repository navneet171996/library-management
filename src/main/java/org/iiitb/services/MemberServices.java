package org.iiitb.services;

import org.iiitb.database.DatabaseServices;
import org.iiitb.entities.Book;
import org.iiitb.entities.Member;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MemberServices {

    private DatabaseServices databaseServices;

    public MemberServices(){
        databaseServices = new DatabaseServices();
    }

    public void searchBooks(String query) {
        try {
            List<Book> allBooksFromDatabase = databaseServices.getAllBooksFromDatabase();
            boolean found = false;
            for (Book book : allBooksFromDatabase) {
                if (book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query)) {
                    System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() +
                            ", Genre: " + book.getGenre() + ", Status: " + (book.getIsIssued() ? "Issued" : "Available"));
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No books match your query.");
            }
        } catch (Exception e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
    }

    public void issueBook(Member member, int id) {
        try {
            Book book = databaseServices.findBookById(id);
            if (book != null) {

                if (book.getIsIssued()) {
                    System.out.println("Error: Book is already issued.");
                } else if (member.getIssuedBooks().contains(book)) {
                    System.out.println("Error: You have already issued this book.");
                } else if (member.getIssuedBooks().size() >= 3) {
                    System.out.println("Error: You cannot issue more than 3 books at a time.");
                } else {
                    book.setIsIssued(true);
                    member.getIssuedBooks().add(book);
                    System.out.println("Book issued successfully!");
                }
            } else {
                System.out.println("Error: Book not found.");
            }
        } catch (Exception e) {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }

    public void returnBook(Member member, int id) {
        try {
            Book book = databaseServices.findBookById(id);
            if (book != null) {

                if (!book.getIsIssued()) {
                    System.out.println("Error: This book was not issued.");
                } else if (!member.getIssuedBooks().contains(book)) {
                    System.out.println("Error: You did not issue this book.");
                } else {
                    book.setIsIssued(false);
                    member.getIssuedBooks().remove(book);
                    System.out.println("Book returned successfully!");
                }
            } else {
                System.out.println("Error: Book not found.");
            }
        } catch (Exception e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }
}
