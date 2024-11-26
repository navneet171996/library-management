package org.iiitb.database;

import org.iiitb.entities.Book;
import org.iiitb.entities.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServices {

    private String url = "jdbc:mysql://localhost:3306/libraryManagement";
    private String username = "root";
    private String password = "Sam@9461027404";

    public void addBookToDatabase(Book book){

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            String query = "INSERT INTO Book (id, title, author, genre, isIssued, price) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Set values for the placeholders
                preparedStatement.setInt(1, book.getId());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setString(3, book.getAuthor());
                preparedStatement.setString(4, book.getGenre());
                preparedStatement.setBoolean(5, false);
                preparedStatement.setDouble(6, book.getPrice());

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("DB: Book added successfully!");
                } else {
                    System.out.println("DB: Failed to add the book.");
                }

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getAllBooksFromDatabase(){
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            List<Book> books = new ArrayList<>();
            String query = "SELECT id, title, author, genre, isIssued, price, status FROM book";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getBoolean("isIssued"),
                        resultSet.getDouble("price"),
                        resultSet.getBoolean("status")
                );
                books.add(book);
            }


            return books;
        }catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Book findBookById(Integer id){
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            String query = "SELECT * FROM book WHERE id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            Book book = new Book();
            if(resultSet.next()){
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setGenre(resultSet.getString("genre"));
                book.setIsIssued(resultSet.getBoolean("isIssued"));
                book.setPrice(resultSet.getDouble("price"));
                book.setStatus(resultSet.getBoolean("status"));
            }
            return book;
        }catch (SQLException e){
            e.printStackTrace();
            return new Book();
        }
    }

    public void deleteBookById(int bookId) {
        String deleteQuery = "DELETE FROM Book WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book with ID " + bookId + " deleted successfully!");
            } else {
                System.out.println("No book found with ID " + bookId + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    public void addMemberToDatabase(Member member) {
        String insertQuery = "INSERT INTO Member (id, name, status) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, member.getId());
            preparedStatement.setString(2, member.getName());
            preparedStatement.setBoolean(3, member.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Member added successfully!");
            } else {
                System.out.println("Failed to add the member.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    public List<Member> getAllMembersFromDatabase() {
        String selectQuery = "SELECT id, name, status FROM Member";
        List<Member> members = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Boolean status = resultSet.getBoolean("status");

                Member member = new Member(id, name, status);
                members.add(member);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving members: " + e.getMessage());
        }

        return members;
    }

    public Member findMemberById(int memberId) {
        String selectQuery = "SELECT id, name, status FROM Member WHERE id = ?";
        Member member = null;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, memberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Boolean status = resultSet.getBoolean("status");

                    member = new Member(id, name, status);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding member: " + e.getMessage());
        }

        return member;
    }

    public List<Book> findBooksByMemberId(int memberId) {
        String query = "SELECT id, title, author, genre, price, isIssued, status FROM Book WHERE member_id = ?";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, memberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    double price = resultSet.getDouble("price");
                    boolean status = resultSet.getBoolean("status");
                    boolean isIssued = resultSet.getBoolean("isIssued");

                    Book book = new Book(id, title, author, genre, isIssued, price, status);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving books for member ID " + memberId + ": " + e.getMessage());
        }

        return books;
    }

    public boolean updateBookPrice(int bookId, double newPrice) {
        String updateQuery = "UPDATE Book SET price = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;
                System.out.println("Book price updated successfully!");
            } else {
                System.out.println("Book with ID " + bookId + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating book price: " + e.getMessage());
        }

        return isUpdated;
    }

    public void updateMemberName(int memberId, String newName) {
        String updateQuery = "UPDATE member SET name = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, memberId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Member name updated successfully!");
            } else {
                System.out.println("Error: Member not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating member name: " + e.getMessage());
        }
    }
}