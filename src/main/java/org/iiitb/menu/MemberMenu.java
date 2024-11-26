package org.iiitb.menu;

import org.iiitb.controller.MemberController;
import org.iiitb.entities.Member;
import org.iiitb.services.MemberServices;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemberMenu {

    Scanner scanner = new Scanner(System.in);
    Map<Integer, Member> members = new HashMap<>();
    MemberController memberController = new MemberController();

    public void viewMemberMenu(){
        try {
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();

            if (!members.containsKey(memberId)) {
                System.out.println("No member found with this ID. Registering a new member...");
                scanner.nextLine(); // consume newline
                System.out.print("Enter Member Name: ");
                String memberName = scanner.nextLine();
                members.put(memberId, new Member(memberId, memberName, true));
                System.out.println("Member registered successfully!");
            }

            Member member = members.get(memberId);

            while (true) {
                System.out.println("\nMember Menu");
                System.out.println("1. Search Books");
                System.out.println("2. Issue Book");
                System.out.println("3. Return Book");
                System.out.println("4. Exit to Main Menu");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 1) {
                    memberController.searchBooks();
                } else if (choice == 2) {
                    memberController.issueBook(member);
                } else if (choice == 3) {
                    memberController.returnBook(member);
                } else if (choice == 4) {
                    break;
                } else {
                    System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in member menu: " + e.getMessage());
        }
    }
}
