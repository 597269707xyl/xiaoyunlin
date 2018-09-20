package com.zdtech.platform.framework.repository;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Repository
public class GenericDao extends BaseDao {

    private static Logger logger = LoggerFactory.getLogger(GenericDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntityManager getEm() {
        return entityManager;
    }

    /**
     * 根据查询条件和过滤条件，查询结果总数
     *
     * @param entity
     * @param queryParams
     * @param extFilter
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object queryTotal(String entity, Map<String, Object> queryParams,
                             String extFilter) {
        String where = getFilter(queryParams);
        if (!StringUtils.isEmpty(extFilter)) {
            if (!StringUtils.isEmpty(where)) {
                where = where + " and " + extFilter;
            } else {
                where = where + " where " + extFilter;
            }
        }
        return querySingle(
                String.format("select count(t) from %s t %s", new Object[]{
                        entity, where}), queryParams);
    }

    @SuppressWarnings("unchecked")
    public List queryRows(String entity, Map<String, Object> queryParams,
                          String extFilter, int pageIndex,
                          int pageSize, String sortField, String sortOrder) {
        String where = getFilter(queryParams);
        if (!StringUtils.isEmpty(extFilter)) {
            if (!StringUtils.isEmpty(where)) {
                where = where + " and " + extFilter;
            } else {
                where = where + " where " + extFilter;
            }
        }


        String jpql = "select t from " + entity + " t" + where;

        String order = getOrder(sortField, sortOrder);
        if (StringUtils.isNotEmpty(order)) {
            jpql = jpql + order;
        }
        logger.info(jpql);
        return queryList(jpql, queryParams, pageIndex * pageSize, pageSize);
    }

    @SuppressWarnings("unchecked")
    public List queryRows(String entity, Map<String, Object> queryParams,
                          String extFilter, String sortField, String sortOrder) {
        String where = getFilter(queryParams);
        if (!StringUtils.isEmpty(extFilter)) {
            if (!StringUtils.isEmpty(where)) {
                where = where + " and " + extFilter;
            } else {
                where = where + " where " + extFilter;
            }
        }
        String jpql = "select t from " + entity + " t" + where;

        String order = getOrder(sortField, sortOrder);
        if (StringUtils.isNotEmpty(order)) {
            jpql = jpql + order;
        }
        logger.info(jpql);
        return queryList(jpql, queryParams);
    }

}
