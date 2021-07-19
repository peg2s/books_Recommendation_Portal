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

    @ManyToMany(mappedBy = "genres", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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
