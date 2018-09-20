package com.zdtech.platform.framework.persistence;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lcheng on 2015/5/14.
 * SearchFilter的工具类
 */
public class SearchFilters {

    private SearchFilters() {
    }

    public static List<SearchFilter> toLikeFilters(Map<String, String> params) {
        List<SearchFilter> filters = new ArrayList<>();
        if (params != null) {
            for (String key : params.keySet()) {
                SearchFilter filter = new SearchFilter(key, SearchFilter.Operator.LIKE, params.get(key));
                filters.add(filter);
            }
        }
        return filters;
    }

    public static List<SearchFilter> toFilter(Map<String, Object> params) {
        List<SearchFilter> filters = new ArrayList<>();
        if (params != null) {
            for (String key : params.keySet()) {
                SearchFilter filter;
                if (key.indexOf('|') > 0) {
                    String[] keyArr = key.split("\\|");
                    filter = new SearchFilter(keyArr[0],getOperator(keyArr[1]),params.get(key));
                }else{
                    filter = new SearchFilter(key, SearchFilter.Operator.EQ, params.get(key));
                }
                filters.add(filter);
            }
        }
        return filters;
    }

    public static <T> Specification toLikeSpecification(Map<String, String> params, Class<T> clazz,
                                                        DynamicSpecifications.ConnType connType) {
        List<SearchFilter> filters = toLikeFilters(params);
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters,connType);
        return spec;
    }

    public static <T> Specification toSpecification(Map<String, Object> params, Class<T> clazz,
                                                    DynamicSpecifications.ConnType connType){
        List<SearchFilter> filters = toFilter(params);
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters, connType);
        return spec;
    }

    private static SearchFilter.Operator getOperator(String op) {
        switch (op) {
            case "=":
                return SearchFilter.Operator.EQ;
            case "!=":
                return SearchFilter.Operator.NEQ;
            case "like":
                return SearchFilter.Operator.LIKE;
            case ">":
                return SearchFilter.Operator.GT;
            case ">=":
                return SearchFilter.Operator.GTE;
            case "<":
                return SearchFilter.Operator.LT;
            case "<=":
                return SearchFilter.Operator.LTE;
            case "is null":
                return SearchFilter.Operator.ISNULL;
            case "is not null":
                return SearchFilter.Operator.ISNOTNULL;
            default:
                return SearchFilter.Operator.EQ;
        }
    }
}
