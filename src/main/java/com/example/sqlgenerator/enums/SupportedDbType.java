package com.example.sqlgenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SupportedDbType {
    POSTGRESQL("postgresql"),
    MYSQL("mysql"),
    ORACLE("oracle");

    private final String type;

    SupportedDbType(String type) {
        this.type = type;
    }

    public static boolean isValid(String testType) {
        return Arrays.stream(SupportedDbType.values())
                .anyMatch(enumType -> enumType.type.equalsIgnoreCase(testType));
    }

    public static SupportedDbType fromString(String text) {
        for (SupportedDbType db : SupportedDbType.values()) {
            if (db.type.equalsIgnoreCase(text)) {
                return db;
            }
        }
        throw new IllegalArgumentException("No enum constant with type '%s' found".formatted(text));
    }
}
