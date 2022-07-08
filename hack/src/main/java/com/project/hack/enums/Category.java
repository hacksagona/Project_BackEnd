package com.project.hack.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum Category {

    //enum 구현중

    HEALTH("건강"),
    HABIT("습관"),
    STUDY("공부"),
    HOBBY("취미");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public String showCategory() {
        return this.value;
    }

}
