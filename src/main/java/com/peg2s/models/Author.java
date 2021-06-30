package com.peg2s.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Table(name="authors")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Author extends AbstractIdentifiableObject{

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "AUTHOR_BOOKS",
            joinColumns = @JoinColumn(name = "AUTHOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID")
    )
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Collection<Book> books = new ArrayList<>();

    public Author(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
