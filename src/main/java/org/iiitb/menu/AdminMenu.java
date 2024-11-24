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
    Map<Integer, Member> members = new HashMap<>();

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
                System.out.println("7. View Issued Books Percentage");
                System.out.println("8. View Most Popular Book");
                System.out.println("9. View Total Fines Collected");
                System.out.println("10. View Average Number of Books Issued Per Member");
                System.out.println("11. View Member with Most Books Issued");
                System.out.println("13. Categorize Books by Genre");
                System.out.println("14. List Members with Multiple Overdue Books");
                System.out.println("15. View Top Genres Based on Issued Books");
                System.out.println("16. Track Inactive Books");
                System.out.println("17. Generate Detailed Genre Report");
                System.out.println("18. Simulate a Day in the Library");
                System.out.println("19. Calculate Average Book Price");
                System.out.println("20. Get Book Details by ID");
                System.out.println("21. Find Books by Genre");
                System.out.println("22. Get Books Issued by a Member");
                System.out.println("23. Check Book Availability");
                System.out.println("24. Extend Book Return Date");
                System.out.println("25. View Total Books Issued by a Genre");
                System.out.println("26. Update Book Price");
                System.out.println("27. Find Books by Author");
                System.out.println("28. Find Most Issued Member");
                System.out.println("29. Calculate Member's Issued Book Percentage");
                System.out.println("30. Generate Member Report");
                System.out.println("31. Compare Member Activity");
                System.out.println("32. Recommend Books to a Member");
                System.out.println("33. Track Most Popular Book");
                System.out.println("34. Archive Old Books");
                System.out.println("35. Generate Library Statistics");
                System.out.println("39. Track Books Issued in Date Range");
                System.out.println("40. Calculate Genre Popularity");
                System.out.println("41. Track Overdue Books");
                System.out.println("42. View Genre-Based Books");
                System.out.println("43. Calculate Average Borrow Time");
                System.out.println("44. Suggest Books to Restock");
                System.out.println("45. Find Inactive Members");
                System.out.println("46. View Book Issue History");
                System.out.println("47. Predict Book Demand");
                System.out.println("48. Add Book Ratings");
                System.out.println("49. Show Top Rated Books");
                System.out.println("50. Track Daily Transactions");
                System.out.println("51. Update Member Details");
                System.out.println("52. View Issued Books by Genre");
                System.out.println("53. Calculate Penalty for Overdue Books");
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
                    case 8:
                        adminController.calculateMostPopularBook();
                        break;
                    case 9:
                        adminController.calculateTotalFines();
                        break;
                    case 13:
                        adminController.categorizeBooksByGenre();
                        break;
                    case 14:
                        adminController.listMembersWithMultipleOverdueBooks();
                        break;
                    case 15:
                        adminController.viewTopGenres();
                        break;
                    case 16:
                        adminController.trackInactiveBooks();
                        break;
                    case 17:
                        adminController.generateDetailedGenreReport();
                        break;
                    case 18:
                        adminController.simulateLibraryDay();
                        break;
                    case 23:
                        adminController.getBookDetailsById();
                        break;
                    case 24:
                        adminController.findBooksByGenre();
                        break;
                    case 25:
                        adminController.getBooksIssuedByMember();
                        break;
                    case 26:
                        adminController.checkBookAvailability();
                        break;
                    case 27:
                        adminController.extendBookReturnDate();
                        break;
                    case 28:
                        adminController.calculateTotalBooksIssuedByGenre();
                        break;
                    case 29:
                        adminController.updateBookPrice();
                        break;
                    case 30:
                        adminController.findBooksByAuthor();
                        break;
                    case 31:
                        Member mostIssuedMember = adminServices.findMostIssuedMember();
                        if (mostIssuedMember != null) {
                            System.out.println("Most Issued Member: " + mostIssuedMember.getName() + " with " + mostIssuedMember.getIssuedBooks().size() + " books.");
                        } else {
                            System.out.println("No members found.");
                        }
                        break;
                    case 32:
                        System.out.print("Enter Member ID: ");
                        int memberId32 = scanner.nextInt();
                        double percentage = adminServices.calculateBooksIssuedByMemberPercentage(memberId32);
                        System.out.println("Books issued by member as percentage of total: " + percentage + "%");
                        break;
                    case 33:
                        System.out.print("Enter Member ID: ");
                        int memberId33 = scanner.nextInt();
                        adminServices.generateMemberReport(memberId33);
                        break;
                    case 34:
                        System.out.print("Enter First Member ID: ");
                        int memberId1 = scanner.nextInt();
                        System.out.print("Enter Second Member ID: ");
                        int memberId2 = scanner.nextInt();
                        adminServices.compareMemberActivity(memberId1, memberId2);
                        break;
                    case 35:
                        System.out.print("Enter Member ID: ");
                        int memberId = scanner.nextInt();
                        adminServices.recommendBooks(memberId);
                        break;
                    case 36:
                        adminServices.trackMostPopularBook();
                        break;
                    case 37:
                        adminServices.archiveOldBooks();
                        break;
                    case 38:
                        adminServices.generateLibraryStatistics();
                        break;
                    case 39:
                        System.out.print("Enter start date (yyyy-mm-dd): ");
                        Date startDate = Date.valueOf(scanner.next());
                        System.out.print("Enter end date (yyyy-mm-dd): ");
                        Date endDate = Date.valueOf(scanner.next());
                        adminServices.trackBooksIssuedInDateRange(startDate, endDate);
                        break;
                    case 40:
                        adminController.calculateGenrePopularity();
                        break;
                    case 41:
                        adminController.trackOverdueBooks();
                        break;
                    case 42:
                        adminController.viewGenreBasedBooks();
                        break;
                    case 43:
                        adminController.calculateAverageBorrowTime();
                        break;
                    case 44:
                        adminController.suggestBooksToRestock();
                        break;
                    case 45:
                        adminController.findInactiveMembers();
                        break;
                    case 46:
                        adminController.viewBookIssueHistory();
                        break;
                    case 47:
                        adminController.predictBookDemand();
                        break;
                    case 48:
                        adminController.addBookRatings();
                        break;
                    case 49:
                        adminController.showTopRatedBooks();
                        break;
                    case 50:
                        adminController.trackDailyTransactions();
                        break;
                    case 51:
                        adminController.updateMemberDetails();
                        break;
                    case 52:
                        adminController.viewIssuedBooksByGenre();
                        break;
                    case 53:
                        System.out.print("Enter Member ID: ");
                        int overdueMemberId = scanner.nextInt();

                        if (members.containsKey(overdueMemberId)) {
                            Member overdueMember = members.get(overdueMemberId);

                            System.out.print("Enter days overdue: ");
                            int daysOverdue = scanner.nextInt();

                            adminServices.calculatePenalty(overdueMember, daysOverdue);
                        } else {
                            System.out.println("Member not found.");
                        }
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
