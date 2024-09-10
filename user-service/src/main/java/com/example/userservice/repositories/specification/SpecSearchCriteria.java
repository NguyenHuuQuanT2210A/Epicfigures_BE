package com.example.userservice.repositories.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.userservice.repositories.specification.SearchOperation.OR_PREDICATE_FLAG;
import static com.example.userservice.repositories.specification.SearchOperation.ZERO_OR_MORE_REGEX;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private Boolean orPredicate;

    public SpecSearchCriteria(String orPredicate, String key, String operation, String value,String prefix, String suffix) {
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper == SearchOperation.EQUALITY){
            boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
            boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

            if (startWithAsterisk && endWithAsterisk){
                oper = SearchOperation.CONTAINS;
            } else if (startWithAsterisk){
                oper = SearchOperation.ENDS_WITH;
            }else if (endWithAsterisk){
                oper = SearchOperation.STARTS_WITH;
            }
        }
        this.key = key;
        this.operation = oper;
        this.value = value;
    }
}
