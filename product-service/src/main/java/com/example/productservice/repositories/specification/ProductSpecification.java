package com.example.productservice.repositories.specification;

import com.example.productservice.entities.Product;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (Boolean.TRUE.equals(criteria.getIsJoinQuery())) {
            return handleJoin(root, query, builder);
        }

        var value = criteria.getValue();
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), value);
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

    private Predicate handleJoin(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var value = criteria.getValue();
        Join<?, Product> join = switch (criteria.getJoinEntity().toLowerCase()) {
            case "category" -> root.join("category");
            default -> throw new IllegalStateException("Unexpected join entity: " + criteria.getJoinEntity());
        };

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(join.get(criteria.getKey()), value);
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
}
