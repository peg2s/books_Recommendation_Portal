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

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private Collection<Author> authors = new ArrayList<>();

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
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
}


