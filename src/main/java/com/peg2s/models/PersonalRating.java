package com.peg2s.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PersonalRating extends AbstractIdentifiableObject{

    private int rate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOK_ID")
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private User user;
}
