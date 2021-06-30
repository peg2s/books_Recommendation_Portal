package com.peg2s.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Sex {
    MALE("Мужской", "М"),
    FEMALE("Женский", "Ж");

    private final String description;
    private final String shortDescription;
}
