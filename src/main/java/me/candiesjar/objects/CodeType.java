package me.candiesjar.objects;

import lombok.Getter;

@Getter
public enum CodeType {
    ;

    private final String name;

    CodeType(String name) {
        this.name = name;
    }
}
