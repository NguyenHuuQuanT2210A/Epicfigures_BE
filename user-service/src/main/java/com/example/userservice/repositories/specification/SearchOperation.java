package com.example.userservice.repositories.specification;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS, JOIN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL;

    public static final String OR_PREDICATE_FLAG = "'";

    public static final String ZERO_OR_MORE_REGEX = "*";

    public static final String COMPARISON_EQUALITY = "=";

    public static SearchOperation getSimpleOperation(final char input) {
        return switch (input) {
            case ':' -> EQUALITY;
            case '!' -> NEGATION;
            case '>' -> GREATER_THAN;
            case '<' -> LESS_THAN;
            case '~' -> LIKE;
            case '-' -> JOIN;
            default -> null;
        };
    }
}
