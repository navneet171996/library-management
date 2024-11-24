package org.iiitb.services;

import org.iiitb.entities.Book;
import org.iiitb.entities.Member;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemberServices {

    Scanner scanner = new Scanner(System.in);
    Map<Integer, Book> bookCatalog = new HashMap<>();

    public void searchBooks() {
        try {
            System.out.print("Enter keyword to search (Title/Author): ");
            scanner.nextLine(); // consume newline
            String query = scanner.nextLine().toLowerCase();

            boolean found = false;
            for (Book book : bookCatalog.values()) {
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

    public void issueBook(Member member) {
        try {
            System.out.print("Enter Book ID to issue: ");
            int id = scanner.nextInt();

            if (bookCatalog.containsKey(id)) {
                Book book = bookCatalog.get(id);

                if (book.getIsIssued()) {
                    System.out.println("Error: Book is already issued.");
                } else if (member.getIssuedBooks().contains(book.getTitle())) {
                    System.out.println("Error: You have already issued this book.");
                } else if (member.getIssuedBooks().size() >= 3) {
                    System.out.println("Error: You cannot issue more than 3 books at a time.");
                } else {
                    book.setIsIssued(true);
                    member.getIssuedBooks().add(book.getTitle());
                    System.out.println("Book issued successfully!");
                }
            } else {
                System.out.println("Error: Book not found.");
            }
        } catch (Exception e) {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }

    public void returnBook(Member member) {
        try {
            System.out.print("Enter Book ID to return: ");
            int id = scanner.nextInt();

            if (bookCatalog.containsKey(id)) {
                Book book = bookCatalog.get(id);

                if (!book.getIsIssued()) {
                    System.out.println("Error: This book was not issued.");
                } else if (!member.getIssuedBooks().contains(book.getTitle())) {
                    System.out.println("Error: You did not issue this book.");
                } else {
                    book.setIsIssued(false);
                    member.getIssuedBooks().remove(book.getTitle());
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
