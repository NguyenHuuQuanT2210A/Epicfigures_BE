package com.example.orderService.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification implements Specification<Order> {
    private SearchCriteria criteria;

    public OrderSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (criteria.getOperation()) {
            case EQUALS:
                return criteriaBuilder.equal(
                        root.get(criteria.getKey()),
                        criteria.getValue());
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(
                        root.get(criteria.getKey()),
                        String.valueOf(criteria.getValue()));
            case GREATER_THAN_OR_EQUALS:
                if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                    return criteriaBuilder.greaterThanOrEqualTo(
                            root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
                } else {
                    return criteriaBuilder.greaterThanOrEqualTo(
                            root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN:
                return criteriaBuilder.lessThan(
                        root.get(criteria.getKey()),
                        String.valueOf(criteria.getValue()));
            case LESS_THAN_OR_EQUALS:
                if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                    return criteriaBuilder.lessThanOrEqualTo(
                            root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
                } else {
                    return criteriaBuilder.lessThanOrEqualTo(
                            root.get(criteria.getKey()), criteria.getValue().toString());
                }
        }
        return null;
    }
}