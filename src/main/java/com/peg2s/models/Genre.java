package com.peg2s.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Genre extends AbstractIdentifiableObject {

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "BOOK_GENRES",
            joinColumns = @JoinColumn(name = "GENRE"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Collection<Book> books = new ArrayList<>();

    private String genre;

    public Genre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }
}
