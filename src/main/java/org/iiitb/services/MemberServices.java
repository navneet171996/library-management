package org.iiitb.services;

import org.iiitb.entities.Book;
import org.iiitb.entities.Member;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemberServices {

    Scanner scanner = new Scanner(System.in);
    Map<Integer, Book> bookCatalog = new HashMap<>();
    public void searchBooks(String query) {
    // du201[def(query)-19 use(query)-23], du202[def(found)-21 use(found)-26,30], du-203[def(book)-22 use(book)-24,25]
        try {
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

    public void issueBook(Member member, int id) {
    //du204[Def(id) - 40, use(id) - 42,43] du205[Def(book)-43, use(book)-45,47,51,54] du206[Def(bookCatalog)-13 use(bookCatalog)-42,43] du206[Def(member)-47 use(member)-51,55]
        try {
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

    public void returnBook(Member member, int id) {
    // du207[Def(id)-67 use(id)-69,90] du208[Def(book)-70 use(book)-72,74,77] du209[Def(bookCatalog)-13 use(bookCatalog)-69,70] du210[Def(member)-65 use(member) - 74,78]
        try {
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
