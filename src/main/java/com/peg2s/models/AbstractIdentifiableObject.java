package com.peg2s.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("PMD")
@MappedSuperclass
public abstract class AbstractIdentifiableObject {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;
}