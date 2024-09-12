package com.example.productservice.repositories.specification;

import com.example.productservice.entities.Category;
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
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case GREATER_THAN_EQUAL:
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN_EQUAL:
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue().toString() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString());
            case CONTAINS:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case JOIN:
                return handleJoin(root, query, builder);
            default:
                throw new IllegalStateException("Unexpected value: " + criteria.getOperation());
        }
    }

    private Predicate handleJoin(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Join with Category entity
        Join<Category, Product> join = root.join("category");
        return builder.equal(join.get(criteria.getKey()), criteria.getValue());
    }
}
