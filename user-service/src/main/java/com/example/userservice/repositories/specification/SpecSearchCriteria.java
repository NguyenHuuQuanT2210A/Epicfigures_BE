package com.example.userservice.repositories.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.userservice.repositories.specification.SearchOperation.OR_PREDICATE_FLAG;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private Boolean orPredicate;
    private Boolean isJoinQuery;
    private String joinEntity;

    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value, Boolean isJoinQuery, String joinEntity) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isJoinQuery = isJoinQuery;
        this.joinEntity = joinEntity;
    }
}
