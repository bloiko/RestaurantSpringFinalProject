package com.restaurant.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MenuFilterBy {
    ALL_CATEGORIES("all_categories"),
    DESSERTS("Desserts"),
    SNACKS("Snacks");

    MenuFilterBy(String value) {
        this.value = value;
    }

    @JsonValue
    private final String value;

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MenuFilterBy deserializeByName(@JsonProperty("name") String name) {
        return MenuFilterBy.valueOf(name);
    }
}
