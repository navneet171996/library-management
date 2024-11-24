package org.iiitb.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private String author;
    private String genre;
    private Boolean isIssued;
    private Double price;

    public Book(Integer id, String title, String author, String genre, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isIssued = false;
        this.price = price;
    }
}
