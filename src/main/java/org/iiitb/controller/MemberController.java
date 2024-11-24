package org.iiitb.controller;

import org.iiitb.entities.Member;
import org.iiitb.services.MemberServices;

import java.util.Scanner;

public class MemberController {

    Scanner scanner = new Scanner(System.in);
    private MemberServices memberServices = new MemberServices();

    public void searchBooks(){
        System.out.print("Enter keyword to search (Title/Author): ");
        scanner.nextLine(); // consume newline
        String query = scanner.nextLine().toLowerCase();

        memberServices.searchBooks(query);
    }

    public void issueBook(Member member){
        System.out.print("Enter Book ID to issue: ");
        int id = scanner.nextInt();

        memberServices.issueBook(member, id);
    }

    public void returnBook(Member member){
        System.out.print("Enter Book ID to return: ");
        int id = scanner.nextInt();

        memberServices.returnBook(member, id);
    }
}
