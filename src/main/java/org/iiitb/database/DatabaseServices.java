package org.iiitb.database;

import org.iiitb.entities.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseServices {

    public void connectToDatabase(){
        String url = "jdbc:mysql://localhost:3306/libraryManagement";
        String username = "root";
        String password = "Priyam123";
    }

    public void addBookToDatabase(Book book){
        String url = "jdbc:mysql://localhost:3306/libraryManagement";
        String username = "root";
        String password = "Priyam123";

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
                    System.out.println("Book added successfully!");
                } else {
                    System.out.println("Failed to add the book.");
                }

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}