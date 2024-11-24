package org.iiitb;

import org.iiitb.menu.AdminMenu;
import org.iiitb.menu.MemberMenu;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        AdminMenu adminMenu = new AdminMenu();
        MemberMenu memberMenu = new MemberMenu();
        try {
            while (true) {
                System.out.println("\nAdvanced Library Management System");
                System.out.println("1. Admin Login");
                System.out.println("2. Member Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 1) {
                    adminMenu.viewAdminMenu();
                } else if (choice == 2) {
                    memberMenu.viewMemberMenu();
                } else if (choice == 3) {
                    System.out.println("Exiting... Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
