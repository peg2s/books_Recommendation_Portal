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

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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
