package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.filter.IQueryFilter;
import com.zdtech.platform.framework.persistence.DynamicSpecifications;
import com.zdtech.platform.framework.persistence.RepQueryDefInfo;
import com.zdtech.platform.framework.persistence.Repositorys;
import com.zdtech.platform.framework.persistence.SearchFilter;
import com.zdtech.platform.framework.repository.GenericDao;
import com.zdtech.platform.framework.repository.TestProjectDao;
import com.zdtech.platform.framework.ui.EntityInfo;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class GenericService {

    @Autowired
    private GenericDao genericDao;
    @Autowired
    private TestProjectDao testProjectDao;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 返回查询结果总数
     *
     * @param entityInfo
     * @param queryParams
     * @return
     */
    public int queryTotal(EntityInfo entityInfo, Map<String, Object> queryParams) {
        String entity = entityInfo.getName();
        String extFilter = null;
        String filterBean = entityInfo.getFilter();
        if (StringUtils.isNotEmpty(filterBean)) {
            IQueryFilter queryFilter = (IQueryFilter) SpringContextUtils
                    .getBean(filterBean);
            extFilter = queryFilter.createExtFilter();
        }

        Object obj = genericDao.queryTotal(entity, queryParams, extFilter);
        return Integer.parseInt(obj.toString());
    }

    /**
     * 返回查询结果
     *
     * @param entityInfo
     * @param queryParams
     * @param page
     * @param rows
     * @return
     */
    public List<Object> queryRows(EntityInfo entityInfo, Map<String, Object> queryParams,
                                  int page, int rows, String sortField, String sortOrder) {
        String entity = entityInfo.getName();
        // 过滤条件
        String extFilter = null;
        String filterBean = entityInfo.getFilter();
        if (StringUtils.isNotEmpty(filterBean)) {
            IQueryFilter queryFilter = (IQueryFilter) SpringContextUtils
                    .getBean(filterBean);
            extFilter = queryFilter.createExtFilter();
        }


        return genericDao.queryRows(entity, queryParams, extFilter,
                page, rows, sortField, sortOrder);
    }

    /**
     * 返回查询结果
     *
     * @param entityInfo
     * @param queryParams
     * @return
     */
    public List<Object> queryRows(EntityInfo entityInfo, Map<String, Object> queryParams,
                                  String sortField, String sortOrder) {
        String entity = entityInfo.getName();
        // 过滤条件
        String extFilter = null;
        String filterBean = entityInfo.getFilter();
        if (StringUtils.isNotEmpty(filterBean)) {
            IQueryFilter queryFilter = (IQueryFilter) SpringContextUtils
                    .getBean(filterBean);
            extFilter = queryFilter.createExtFilter();
        }


        return genericDao.queryRows(entity, queryParams, extFilter,
                sortField, sortOrder);
    }

    /**
     * 通用查询。通过在Dao上定义查询，以实现通用的基于entity属性进行查询
     *
     * @param entity
     * @param params
     * @param page
     * @return
     */
    public Map<String, Object> commonQuery(String entity, Map<String, Object> params, Pageable page) {
        Map<String, Object> map = new HashMap<>();
        cleanParams(params);
        RepQueryDefInfo defInfo = Repositorys.getRepQueryDefInfo(entity);
        Object daoObj = defInfo.getDao();
        Page data = null;
        Sort sort = null;
        if (defInfo.getSortFields() != null && defInfo.getSortFields().length > 0) {
            sort = new Sort(defInfo.getDirection(), defInfo.getSortFields());
        }

        if (params.size() > 0) {
            List<SearchFilter> filters = new ArrayList<>();
            defInfo.getGenericQueryFields();
            for (String queryField : params.keySet()) {
                Object val = params.get(queryField);
                if (val.toString().equals("true")){
                    val = true;
                }else if (val.toString().equals("false")){
                    val = false;
                }
                SearchFilter filter = null;
                filter = processToFilter(queryField, val);
                if (filter.fieldName.equals("createDate") && null != filter.value) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date dates = format.parse((filter.value).toString());
                        filter.value = dates;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (filter.fieldName.contains("Time")&& null != filter.value){
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (null != filter.value){
                            filter.value = format.parse((filter.value).toString());
                        }
                        if (null != filter.value1){
                            filter.value1 = format.parse((filter.value1).toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (filter != null) filters.add(filter);
            }
            Specification spec = DynamicSpecifications.bySearchFilter(filters,
                     DynamicSpecifications.ConnType.And);
            if (daoObj instanceof JpaSpecificationExecutor) {
                JpaSpecificationExecutor dao = (JpaSpecificationExecutor) daoObj;
                if (page != null) {
                    data = dao.findAll(spec, new PageRequest(page.getPageNumber(), page.getPageSize(), sort));
                } else {
                    List rawData = dao.findAll(spec, sort);
                    data = new PageImpl(rawData);
                }

            }
        } else {
            if (daoObj instanceof JpaRepository) {
                JpaRepository dao = (JpaRepository) daoObj;
                if (page != null) {
                    data = dao.findAll(new PageRequest(page.getPageNumber(), page.getPageSize(), sort));
                } else {
                    List rawData = dao.findAll(sort);
                    data = new PageImpl(rawData);
                }
            }
        }
        if (data != null) {
            map.put("rows", data.getContent());
            map.put("total", data.getTotalElements());
        }
        return map;
    }

    public Map<String, Object> queryForAuz(String entity, Map<String, Object> params, Pageable page, Long userId) {
        Map<String, Object> map = new HashMap<>();
        cleanParams(params);
        RepQueryDefInfo defInfo = Repositorys.getRepQueryDefInfo(entity);
        Object daoObj = defInfo.getDao();
        Page data = null;
        Sort sort = null;
        if (defInfo.getSortFields() != null && defInfo.getSortFields().length > 0) {
            sort = new Sort(defInfo.getDirection(), defInfo.getSortFields());
        }

        if (params.size() > 0) {
            List<SearchFilter> filters = new ArrayList<>();
            defInfo.getGenericQueryFields();
            for (String queryField : params.keySet()) {
                Object val = params.get(queryField);
                SearchFilter filter = null;
                filter = processToFilter(queryField, val);
                if (filter.fieldName.equals("createDate") && null != filter.value) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date dates = format.parse((filter.value).toString());
                        filter.value = dates;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (filter != null) filters.add(filter);
            }
            Specification spec = DynamicSpecifications.bySearchFilter(filters,
                    DynamicSpecifications.ConnType.And);
            if (daoObj instanceof JpaSpecificationExecutor) {
                JpaSpecificationExecutor dao = (JpaSpecificationExecutor) daoObj;
                if (page != null) {
                    data = testProjectDao.findAll(spec, new PageRequest(page.getPageNumber(), page.getPageSize(), sort));
                } else {
                    List rawData = testProjectDao.findAll(spec, sort);
                    data = new PageImpl(rawData);
                }

            }
        } else {
            if (daoObj instanceof JpaRepository) {
                JpaRepository dao = (JpaRepository) daoObj;
                if (page != null) {
                    data = new PageImpl(testProjectDao.findByUser(userId));
                } else {
                    List rawData = testProjectDao.findAll(sort);
                    data = new PageImpl(rawData);
                }
            }
        }
        if (data != null) {
            map.put("rows", data.getContent());
            map.put("total", data.getTotalElements());
        }
        return map;
    }

    public Map<String, Object> queryForNameParams(String sql, String countSql, Map<String, Object> ps, Pageable page) {
        cleanParams(ps);
        Query query = entityManager.createQuery(sql);
        setNamedParams(query, ps);

        List tasks = null;
        if (page != null) {
            tasks = query.setFirstResult(page.getOffset()).setMaxResults(page.getPageSize()).getResultList();
        } else {
            tasks = query.getResultList();
        }

        Map<String, Object> result = new HashMap();
        result.put("rows", tasks);

        query = entityManager.createQuery(countSql);
        setNamedParams(query, ps);
        Long count = (Long) query.getSingleResult();
        result.put("total", count);
        return result;
    }

    private SearchFilter processToFilter(String field, Object value) {
        if (value != null) {
            String[] f = field.split("\\|");
            SearchFilter filter = null;
            if (f.length == 1) {
                filter = new SearchFilter(f[0], SearchFilter.Operator.EQ, value);
                return filter;
            }
            if (f.length >= 2) {
                String fn = f[0];
                String op = f[1];
                switch (op) {
                    case ">":
                        filter = new SearchFilter(fn, SearchFilter.Operator.GT, value);
                        break;
                    case ">=":
                        filter = new SearchFilter(fn, SearchFilter.Operator.GTE, value);
                        break;
                    case "<":
                        filter = new SearchFilter(fn, SearchFilter.Operator.LT, value);
                        break;
                    case "<=":
                        filter = new SearchFilter(fn, SearchFilter.Operator.LTE, value);
                        break;
                    case "like":
                        filter = new SearchFilter(fn, SearchFilter.Operator.LIKE, value);
                        break;
                    case "!=":
                        filter = new SearchFilter(fn, SearchFilter.Operator.NEQ, value);
                        break;
                    case "between":
                        String[] s = value.toString().split(",");
                        value = s[0];
                        Object value1 = s[1];
                        filter = new SearchFilter(fn, SearchFilter.Operator.BET, value, value1);
                        break;
                }
            }
            return filter;
        } else {
            return null;
        }
    }

    private void cleanParams(Map<String, Object> params) {
        if (params != null) {
            params.remove("page");
            params.remove("sort");
            params.remove("order");
            params.remove("rows");
            params.remove("size");
        }
    }

    private void setNamedParams(Query query, Map<String, Object> ps) {
        if (ps != null) {
            for (String key : ps.keySet()) {
                String[] ka = key.split("\\|");
                if (ka.length == 1) {
                    query.setParameter(key, ps.get(key));
                } else if (ka.length > 1) {
                    String op = ka[1];
                    if (op.equals("like")) {
                        String value = "%" + ps.get(key) + "%";
                        query.setParameter(ka[0], value);
                    } else if (op.equalsIgnoreCase("is null") || op.equalsIgnoreCase("is not null")) {
                        //do nothing.
                    } else {
                        query.setParameter(ka[0], ps.get(key));
                    }
                }
            }
        }
    }
}
