package com.peg2s.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="books")
@EqualsAndHashCode(callSuper = true)
public class Book extends AbstractIdentifiableObject implements Serializable {

    private String title;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "AUTHOR_BOOKS",
            joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID")
    )
    private Collection<Author> authors = new ArrayList<>();


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "BOOK_GENRES",
            joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE"))
    private Collection<Genre> genres = new ArrayList<>();

    private String year;

    private String description;

    private String annotation;

    private Double avgRating;

    private Boolean isApproved;

    public String getAuthorsAsString() {
        return authors.stream().map(Author::getName).collect(Collectors.joining(", "));
    }

    public String getCroppedAnnotation() {
        if (annotation.length() > 150) {
            String croppedAnnotation = annotation.substring(0, 150);
            return croppedAnnotation.substring(0, croppedAnnotation.lastIndexOf(" ")).concat("...");
        }
        return annotation;
    }

    public boolean findAnyMatch(String text) {
        return title.contains(text) || getAuthorsAsString().contains(text)
                || description.contains(text) || annotation.contains(text);
    }
}


