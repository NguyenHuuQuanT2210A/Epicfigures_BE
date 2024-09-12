package com.example.productservice.util;

public interface AppConst {
    String SEARCH_SPEC_OPERATOR = "(\\p{Punct}?)(\\w+?)(\\p{Punct}?)([<:>~!-])(\\p{Punct}?)(.*)";
    String SORT_BY = "(\\w+?)(:)(asc|desc)";
}
