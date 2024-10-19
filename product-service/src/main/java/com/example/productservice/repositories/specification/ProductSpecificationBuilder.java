package com.example.productservice.repositories.specification;

import com.example.productservice.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.example.productservice.repositories.specification.SearchOperation.*;

public class ProductSpecificationBuilder {

    public final List<SpecSearchCriteria> params;

    public ProductSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public ProductSpecificationBuilder with(String prefix, String key, String suffix, String operation, String operation2, String value, Boolean isJoinQuery, String joinEntity) {
        return with(null, prefix, key, suffix, operation, operation2, value, isJoinQuery, joinEntity);
    }

    public ProductSpecificationBuilder with(String orPredicate, String prefix, String key, String suffix, String operation, String operation2, String value, Boolean isJoinQuery, String joinEntity) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper != null) {
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
            } else if ((oper == SearchOperation.GREATER_THAN || oper == SearchOperation.LESS_THAN) && operation2.equals(COMPARISON_EQUALITY)) {
                oper = (oper == SearchOperation.GREATER_THAN) ? SearchOperation.GREATER_THAN_EQUAL : SearchOperation.LESS_THAN_EQUAL;
            }
            params.add(new SpecSearchCriteria(orPredicate, key, oper, value, isJoinQuery, joinEntity));
        }
        return this;
    }

    public Specification<Product> build(){
        Specification<Product> specification = new ProductSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            specification = params.get(i).getOrPredicate()
                    ? Specification.where(specification).or(new ProductSpecification(params.get(i)))
                    : Specification.where(specification).and(new ProductSpecification(params.get(i)));
        }
        return specification;
    }
}
