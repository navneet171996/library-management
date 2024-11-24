package org.iiitb.database;

import org.iiitb.entities.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServices {

    private String url = "jdbc:mysql://localhost:3306/libraryManagement";
    private String username = "root";
    private String password = "Priyam123";

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
            String query = "SELECT id, title, author, genre, isIssued, price FROM book";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getBoolean("isIssued"),
                        resultSet.getDouble("price")
                );
                books.add(book);
            }


            return books;
        }catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}