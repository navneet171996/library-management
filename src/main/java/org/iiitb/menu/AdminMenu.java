package org.iiitb.menu;

import org.iiitb.controller.AdminController;
import org.iiitb.entities.Member;
import org.iiitb.services.AdminServices;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {

    Scanner scanner = new Scanner(System.in);
    AdminServices adminServices = new AdminServices();
    AdminController adminController = new AdminController();

    public void viewAdminMenu() {
        try {
            while (true) {
                System.out.println("\nAdmin Menu");
                System.out.println("1. Add Book");
                System.out.println("2. Delete Book");
                System.out.println("3. View All Books");
                System.out.println("4. View Total Books in Catalog");
                System.out.println("5. View Total Library Value");
                System.out.println("6. View Total Issued Books");
                System.out.println("7. View Most Popular Book");
                System.out.println("8. Categorize Books by Genre");
                System.out.println("9. Get Book Details by ID");
                System.out.println("10. Find Books by Genre");
                System.out.println("11. Get Books Issued by a Member");
                System.out.println("12. Check Book Availability");
                System.out.println("13. View Total Books Issued by a Genre");
                System.out.println("14. Update Book Price");
                System.out.println("15. Find Books by Author");
                System.out.println("16. Find Most Issued Member");
                System.out.println("17. Calculate Genre Popularity");
                System.out.println("18. Find Inactive Members");
                System.out.println("19. Update Member Details");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        adminController.addBook();
                        break;
                    case 2:
                        adminController.deleteBook();
                        break;
                    case 3:
                        adminController.viewAllBooks();
                        break;
                    case 4:
                        adminController.calculateTotalBooks();
                        break;
                    case 5:
                        adminController.calculateTotalLibraryValue();
                        break;
                    case 6:
                        adminController.calculateTotalIssuedBooks();
                        break;
                    case 7:
                        adminController.calculateMostPopularBook();
                        break;
                    case 8:
                        adminController.categorizeBooksByGenre();
                        break;
                    case 9:
                        adminController.getBookDetailsById();
                        break;
                    case 10:
                        adminController.findBooksByGenre();
                        break;
                    case 11:
                        adminController.getBooksIssuedByMember();
                        break;
                    case 12:
                        adminController.checkBookAvailability();
                        break;
                    case 13:
                        adminController.calculateTotalBooksIssuedByGenre();
                        break;
                    case 14:
                        adminController.updateBookPrice();
                        break;
                    case 15:
                        adminController.findBooksByAuthor();
                        break;
                    case 16:
                        Member mostIssuedMember = adminServices.findMostIssuedMember();
                        if (mostIssuedMember != null) {
                            System.out.println("Most Issued Member: " + mostIssuedMember.getName() + " with " + mostIssuedMember.getIssuedBooks().size() + " books.");
                        } else {
                            System.out.println("No members found.");
                        }
                        break;
                    case 17:
                        adminController.calculateGenrePopularity();
                        break;
                    case 18:
                        adminController.findInactiveMembers();
                        break;
                    case 19:
                        adminController.updateMemberDetails();
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in admin menu: " + e.getMessage());
            scanner.nextLine(); // Clear the buffer
        }
    }
}
