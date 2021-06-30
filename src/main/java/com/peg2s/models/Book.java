package com.peg2s.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="books")
public class Book extends AbstractIdentifiableObject implements Serializable {

    private String title;

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private Collection<Author> authors = new ArrayList<>();

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private Collection<Genre> genres = new ArrayList<>();

    private String year;

    private String description;

    private String annotation;

    private Double avgRating;

    private int personalRating;
}


