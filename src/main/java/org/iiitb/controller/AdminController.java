package org.iiitb.controller;

import org.iiitb.entities.Book;
import org.iiitb.services.AdminServices;

import java.sql.Date;
import java.util.Scanner;

public class AdminController {

    Scanner scanner = new Scanner(System.in);

    private AdminServices adminServices = new AdminServices();

    public void addBook(){

        Book book = new Book();

        System.out.print("Enter Book ID: ");
        book.setId(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter Title: ");
        book.setTitle(scanner.nextLine());

        System.out.print("Enter Author: ");
        book.setAuthor(scanner.nextLine());

        System.out.print("Enter Genre: ");
        book.setGenre(scanner.nextLine());

        System.out.print("Enter Price: ");
        book.setPrice(scanner.nextDouble());

        book.setStatus(true);

        adminServices.addBook(book);
    }

    public void deleteBook(){
        System.out.print("Enter Book ID to delete: ");
        int id = scanner.nextInt();

        adminServices.deleteBook(id);
    }

    public void viewAllBooks(){
        adminServices.viewAllBooks();
    }

    public void calculateTotalBooks(){
        adminServices.calculateTotalBooks();
    }

    public void calculateTotalLibraryValue(){
        adminServices.calculateTotalLibraryValue();
    }

    public void calculateTotalIssuedBooks(){
        adminServices.calculateTotalIssuedBooks();
    }

    public void calculateMostPopularBook(){
        adminServices.calculateMostPopularBook();
    }

    public void getBookDetailsById(){
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        adminServices.getBookDetailsById(bookId);
    }

    public void findBooksByGenre(){
        System.out.print("Enter Genre: ");
        scanner.nextLine();
        String genre = scanner.nextLine();
        adminServices.findBooksByGenre(genre);
    }

    public void getBooksIssuedByMember(){
        System.out.print("Enter Member ID: ");
        int memberId = scanner.nextInt();
        adminServices.getBooksIssuedByMember(memberId);
    }

    public void checkBookAvailability(){
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        boolean available = adminServices.checkBookAvailability(bookId);
        System.out.println("Book availability: " + (available ? "Available" : "Issued"));
    }

    public void calculateTotalBooksIssuedByGenre(){
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        int totalBooksIssued = adminServices.calculateTotalBooksIssuedByGenre(genre);
        System.out.println("Total books issued in genre '" + genre + "': " + totalBooksIssued);
    }

    public void updateBookPrice(){
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter New Price: ");
        double newPrice = scanner.nextDouble();
        adminServices.updateBookPrice(bookId, newPrice);
        System.out.println("Book price updated successfully.");
    }

    public void findBooksByAuthor(){
        System.out.print("Enter Author Name: ");
        scanner.nextLine();
        String author = scanner.nextLine();
        adminServices.findBooksByAuthor(author);
    }

    public void calculateGenrePopularity(){
        adminServices.calculateGenrePopularity();
    }

    public void findInactiveMembers(){
        adminServices.findInactiveMembers();
    }

    public void updateMemberDetails(){
        System.out.print("Enter Member ID: ");
        int updateMemberId = scanner.nextInt();
        System.out.print("Enter New Name: ");
        scanner.nextLine();
        String newName = scanner.nextLine();
        adminServices.updateMemberDetails(updateMemberId, newName);

    }

    public void categorizeBooksByGenre(){
        adminServices.categorizeBooksByGenre();
    }
}
