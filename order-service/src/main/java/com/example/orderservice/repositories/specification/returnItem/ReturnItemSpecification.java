package com.example.orderservice.repositories.specification.returnItem;

import com.example.orderservice.entities.ReturnItem;
import com.example.orderservice.repositories.specification.SpecSearchCriteria;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
public class ReturnItemSpecification implements Specification<ReturnItem> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<ReturnItem> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (Boolean.TRUE.equals(criteria.getIsJoinQuery())) {
            return handleJoin(root, query, builder);
        }

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

    private Predicate handleJoin(Root<ReturnItem> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var value = criteria.getValue();
        Join<?, ReturnItem> join = switch (criteria.getJoinEntity().toLowerCase()) {
            case "order_detail" -> root.join("orderDetail");
            default -> throw new IllegalStateException("Unexpected join entity: " + criteria.getJoinEntity());
        };

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(join.get(criteria.getKey()), value);
            case BOOLEAN_EQUALITY -> builder.equal(root.get(criteria.getKey()), Boolean.parseBoolean(value.toString()));
            case NEGATION -> builder.notEqual(join.get(criteria.getKey()), value);
            case GREATER_THAN -> builder.greaterThan(join.get(criteria.getKey()), value.toString());
            case GREATER_THAN_EQUAL ->
                    builder.greaterThanOrEqualTo(join.get(criteria.getKey()), value.toString());
            case LESS_THAN -> builder.lessThan(join.get(criteria.getKey()), value.toString());
            case LESS_THAN_EQUAL ->
                    builder.lessThanOrEqualTo(join.get(criteria.getKey()), value.toString());
            case LIKE -> builder.like(join.get(criteria.getKey()), "%" + value.toString() + "%");
            case STARTS_WITH -> builder.like(join.get(criteria.getKey()), value.toString() + "%");
            case ENDS_WITH -> builder.like(join.get(criteria.getKey()), "%" + value.toString());
            case CONTAINS -> builder.like(join.get(criteria.getKey()), "%" + value + "%");
            default -> throw new IllegalStateException("Unexpected value: " + criteria.getOperation());
        };
    }

//    private Predicate handleJoin(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//        Join<Role, User> join = root.join("roles");
//        return builder.equal(join.get(criteria.getKey()), criteria.getValue());
//    }
}
