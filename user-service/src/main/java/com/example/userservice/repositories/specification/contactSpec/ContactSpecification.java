package com.example.userservice.repositories.specification.contactSpec;

import com.example.userservice.entities.Contact;
import com.example.userservice.repositories.specification.SpecSearchCriteria;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
public class ContactSpecification implements Specification<Contact> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Contact> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var value = criteria.getValue();

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), value);
            case BOOLEAN_EQUALITY -> builder.equal(root.get(criteria.getKey()), Boolean.parseBoolean(value.toString()));
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), value);
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), value.toString());
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(root.get(criteria.getKey()), value.toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), value.toString());
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(root.get(criteria.getKey()), value.toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + value.toString() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), value.toString() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + value.toString());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + value + "%");
            default -> throw new IllegalStateException("Unexpected value: " + criteria.getOperation());
        };
    }
}
