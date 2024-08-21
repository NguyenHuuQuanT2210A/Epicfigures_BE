package com.example.userservice.specification;

public class SearchCriteria {
    private String key; // need filtering field
    private SearchCriteriaOperator operation; // comparative operator ( <, <= ...)
    private Object value; // value for compare

    public SearchCriteria() {

    }

    public SearchCriteria(final String key, final SearchCriteriaOperator operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public SearchCriteriaOperator getOperation() {
        return operation;
    }

    public void setOperation(final SearchCriteriaOperator operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

}

