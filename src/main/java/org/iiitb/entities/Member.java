package org.iiitb.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Member {
    private Integer id;
    private String name;
    private List<Book> issuedBooks;
    private Boolean status;

    public Member(Integer id, String name, Boolean status) {
        this.id = id;
        this.name = name;
        this.issuedBooks = new ArrayList<>();
    }
}
