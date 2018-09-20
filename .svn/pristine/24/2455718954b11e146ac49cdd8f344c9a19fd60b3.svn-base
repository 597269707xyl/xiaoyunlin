package com.zdtech.platform.framework.persistence;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

public class DynamicSpecifications {

    public enum ConnType {
        And, Or
    }

    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters,
                                                      final ConnType connType) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (filters != null && filters.size() > 0) {

                    List<Predicate> predicates = Lists.newArrayList();
                    for (SearchFilter filter : filters) {
                        // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicates.add(builder.equal(expression, filter.value));
                                break;
                            case NEQ:
                                predicates.add(builder.notEqual(expression, filter.value));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case GT:
                                predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                                break;
                            case LT:
                                predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                                break;
                            case GTE:
                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case LTE:
                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case BET:
                                predicates.add(builder.between(expression, (Comparable) filter.value, (Comparable) filter.value1));
                                break;
                            case ISNULL:
                                predicates.add(builder.isNull(expression));
                                break;
                            case ISNOTNULL:
                                predicates.add(builder.isNotNull(expression));
                                break;
                        }
                    }

                    // 将所有条件用 and 联合起来
                    if (!predicates.isEmpty()) {
                        if (connType == ConnType.Or) {
                            return builder.or(predicates.toArray(new Predicate[predicates.size()]));
                        } else {
                            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                        }
                    }
                }

                return builder.conjunction();
            }
        };
    }


    public static <T> Specification<T> bySearchFilter1(final Collection<SearchFilter> filters, final Collection<SearchFilter> filters1,
                                                       final ConnType connType) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates1 = Lists.newArrayList();
                if (filters1 != null && filters1.size() > 0) {

                    for (SearchFilter filter : filters1) {
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicates1.add(builder.equal(expression, filter.value));
                                break;
                            case NEQ:
                                predicates1.add(builder.notEqual(expression, filter.value));
                                break;
                            case LIKE:
                                predicates1.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case GT:
                                predicates1.add(builder.greaterThan(expression, (Comparable) filter.value));
                                break;
                            case LT:
                                predicates1.add(builder.lessThan(expression, (Comparable) filter.value));
                                break;
                            case GTE:
                                predicates1.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case LTE:
                                predicates1.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case BET:
                                predicates1.add(builder.between(expression, (Comparable) filter.value, (Comparable) filter.value1));
                                break;
                            case ISNULL:
                                predicates1.add(builder.isNull(expression));
                                break;
                            case ISNOTNULL:
                                predicates1.add(builder.isNotNull(expression));
                                break;
                        }
                    }
                }


                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.or(predicates1.toArray(new Predicate[predicates1.size()])));
                for (SearchFilter filter : filters) {
                    // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                    String[] names = StringUtils.split(filter.fieldName, ".");
                    Path expression = root.get(names[0]);
                    for (int i = 1; i < names.length; i++) {
                        expression = expression.get(names[i]);
                    }
                    // logic operator
                    switch (filter.operator) {
                        case EQ:
                            predicates.add(builder.equal(expression, filter.value));
                            break;
                        case NEQ:
                            predicates.add(builder.notEqual(expression, filter.value));
                            break;
                        case LIKE:
                            predicates.add(builder.like(expression, "%" + filter.value + "%"));
                            break;
                        case GT:
                            predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                            break;
                        case LT:
                            predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                            break;
                        case GTE:
                            predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                            break;
                        case LTE:
                            predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                            break;
                        case BET:
                            predicates.add(builder.between(expression, (Comparable) filter.value, (Comparable) filter.value1));
                            break;
                        case ISNULL:
                            predicates.add(builder.isNull(expression));
                            break;
                        case ISNOTNULL:
                            predicates.add(builder.isNotNull(expression));
                            break;
                    }
                }

                // 将所有条件用 and 联合起来
                if (!predicates.isEmpty()) {
                    if (connType == ConnType.Or) {
                        return builder.or(predicates.toArray(new Predicate[predicates.size()]));
                    } else {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }


                return builder.conjunction();
            }
        };
    }
}
