package org.iiitb.menu;

import org.iiitb.entities.Member;
import org.iiitb.services.AdminServices;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {

    Scanner scanner = new Scanner(System.in);
    AdminServices adminServices = new AdminServices();
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
                        adminServices.addBook();
                        break;
                    case 2:
                        adminServices.deleteBook();
                        break;
                    case 3:
                        adminServices.viewAllBooks();
                        break;
                    case 4:
                        adminServices.calculateTotalBooks();
                        break;
                    case 5:
                        adminServices.calculateTotalLibraryValue();
                        break;
                    case 6:
                        adminServices.calculateTotalIssuedBooks();
                        break;
                    case 7:
                        adminServices.calculateIssuedBooksPercentage();
                        break;
                    case 8:
                        adminServices.calculateMostPopularBook();
                        break;
                    case 9:
                        adminServices.calculateTotalFines();
                        break;
                    case 10:
                        adminServices.calculateAverageBooksIssued();
                        break;
                    case 11:
                        adminServices.findMemberWithMostIssuedBooks();
                        break;
                    case 13:
                        adminServices.categorizeBooksByGenre();
                        break;
                    case 14:
                        adminServices.listMembersWithMultipleOverdueBooks();
                        break;
                    case 15:
                        adminServices.viewTopGenres();
                        break;
                    case 16:
                        adminServices.trackInactiveBooks();
                        break;
                    case 17:
                        adminServices.generateDetailedGenreReport();
                        break;
                    case 18:
                        adminServices.simulateLibraryDay();
                        break;
                    case 22:
                        adminServices.calculateAverageBookPrice();
                        break;
                    case 23:
                        System.out.print("Enter Book ID: ");
                        int bookId = scanner.nextInt();
                        adminServices.getBookDetailsById(bookId);
                        break;
                    case 24:
                        System.out.print("Enter Genre: ");
                        scanner.nextLine(); // Consume newline
                        String genre = scanner.nextLine();
                        adminServices.findBooksByGenre(genre);
                        break;
                    case 25:
                        System.out.print("Enter Member ID: ");
                        int memberId = scanner.nextInt();
                        adminServices.getBooksIssuedByMember(memberId);
                        break;
                    case 26:
                        System.out.print("Enter Book ID: ");
                        bookId = scanner.nextInt();
                        boolean available = adminServices.checkBookAvailability(bookId);
                        System.out.println("Book availability: " + (available ? "Available" : "Issued"));
                        break;
                    case 27:
                        System.out.print("Enter Book ID: ");
                        bookId = scanner.nextInt();
                        System.out.print("Enter Member ID: ");
                        memberId = scanner.nextInt();
                        adminServices.extendBookReturnDate(bookId, memberId);
                        break;
                    case 28:
                        System.out.print("Enter Genre: ");
                        genre = scanner.nextLine();
                        int totalBooksIssued = adminServices.calculateTotalBooksIssuedByGenre(genre);
                        System.out.println("Total books issued in genre '" + genre + "': " + totalBooksIssued);
                        break;
                    case 29:
                        System.out.print("Enter Book ID: ");
                        bookId = scanner.nextInt();
                        System.out.print("Enter New Price: ");
                        double newPrice = scanner.nextDouble();
                        adminServices.updateBookPrice(bookId, newPrice);
                        System.out.println("Book price updated successfully.");
                        break;
                    case 30:
                        System.out.print("Enter Author Name: ");
                        scanner.nextLine(); // Consume newline
                        String author = scanner.nextLine();
                        adminServices.findBooksByAuthor(author);
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
                        memberId = scanner.nextInt();
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
                        adminServices.calculateGenrePopularity();
                        break;
                    case 41:
                        adminServices.trackOverdueBooks();
                        break;
                    case 42:
                        adminServices.viewGenreBasedBooks();
                        break;
                    case 43:
                        adminServices.calculateAverageBorrowTime();
                        break;
                    case 44:
                        adminServices.suggestBooksToRestock();
                        break;
                    case 45:
                        adminServices.findInactiveMembers();
                        break;
                    case 46:
                        System.out.print("Enter Book ID: ");
                        int bookHistoryId = scanner.nextInt();
                        adminServices.viewBookIssueHistory(bookHistoryId);
                        break;
                    case 47:
                        adminServices.predictBookDemand();
                        break;
                    case 48:
                        System.out.print("Enter Member ID: ");
                        int ratingMemberId = scanner.nextInt();
                        System.out.print("Enter Book ID: ");
                        int ratingBookId = scanner.nextInt();
                        System.out.print("Enter Rating (1-5): ");
                        int rating = scanner.nextInt();
                        adminServices.addBookRatings(ratingMemberId, ratingBookId, rating);
                        break;
                    case 49:
                        adminServices.showTopRatedBooks();
                        break;
                    case 50:
                        System.out.print("Enter date (yyyy-mm-dd): ");
                        Date date = Date.valueOf(scanner.next());
                        adminServices.trackDailyTransactions(date);
                        break;
                    case 51:
                        System.out.print("Enter Member ID: ");
                        int updateMemberId = scanner.nextInt();
                        System.out.print("Enter New Name: ");
                        scanner.nextLine(); // Consume newline
                        String newName = scanner.nextLine();
                        System.out.print("Enter New Contact: ");
                        String newContact = scanner.nextLine();
                        adminServices.updateMemberDetails(updateMemberId, newName);
                        break;
                    case 52:
                        System.out.print("Enter Genre: ");
                        scanner.nextLine(); // Consume newline
                        String genreBooks = scanner.nextLine();
                        adminServices.viewIssuedBooksByGenre(genreBooks);
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
