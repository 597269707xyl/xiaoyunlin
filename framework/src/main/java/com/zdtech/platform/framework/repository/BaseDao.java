package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.utils.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用数据访问接口
 *
 * @author qfxu
 */
public abstract class BaseDao<T, ID extends Serializable> {
    private Class<T> entityClass;

    private static Logger logger = LoggerFactory.getLogger(BaseDao.class);

    public abstract EntityManager getEm();

    public Session getSession() {
        return (Session) getEm().getDelegate();
    }

    public BaseDao() {
        this.entityClass = Reflections.getClassGenricType(super.getClass());
    }


    @SuppressWarnings("unchecked")
    public T querySingle(String qlString, Map<String, Object> filter) {
        logger.info(qlString);
        Query query = getEm().createQuery(qlString);
        if (filter != null) {
            for (String key : filter.keySet()) {
                if(key.equals("start")||key.equals("end")){
                    continue;
                }else {
                    if (key.equals("createTime")) {
                        if(","!=filter.get(key)&&null!=filter.get(key)&&!filter.get(key).equals(",")) {
                            String[] dv = (filter.get(key).toString()).split(",");
                            if (dv.length > 1 && null != dv[0] && !dv[0].equals("") && null != dv[1] && !dv[1].equals("")) {
                                query.setParameter(getFieldNameNoKey("start"), filter.get("start"));
                                query.setParameter(getFieldNameNoKey("end"), filter.get("end"));
                            } else {
                                if (null != dv[0] && !dv[0].equals("")) {
                                    query.setParameter(getFieldNameNoKey("start"), filter.get("start"));
                                } else if (null != dv[1] && !dv[1].equals("")) {
                                    query.setParameter(getFieldNameNoKey("end"), filter.get("end"));
                                }
                            }
                        }
                    } else {
                        query.setParameter(getFieldNameNoKey(key), filter.get(key));
                    }
                }
            }
        }
        return (T) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<T> queryList(String qlString, Object... params) {
        Query query = getEm().createQuery(qlString);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> queryList(String qlString, Map<String, Object> filter) {
        logger.debug("queryList(): query[" + qlString + "]");
        Query query = getEm().createQuery(qlString);
        if (filter != null) {
            for (String key : filter.keySet()) {
                if (null != filter.get(key) && !"".equals(filter.get(key))) {
                    if (key.equals("start") || key.equals("end")) {
                        continue;
                    } else {
                        if (key.equals("createTime")) {
                            if ("," != filter.get(key) && null != filter.get(key) && !filter.get(key).equals(",")) {
                                String[] dv = (filter.get(key).toString()).split(",");
                                if (dv.length > 1 && null != dv[0] && !dv[0].equals("") && null != dv[1] && !dv[1].equals("")) {
                                    query.setParameter(getFieldNameNoKey("start"), filter.get("start"));
                                    query.setParameter(getFieldNameNoKey("end"), filter.get("end"));
                                } else {
                                    if (null != dv[0] && !dv[0].equals("")) {
                                        query.setParameter(getFieldNameNoKey("start"), filter.get("start"));
                                    } else if (null != dv[1] && !dv[1].equals("")) {
                                        query.setParameter(getFieldNameNoKey("end"), filter.get("end"));
                                    }
                                }
                            }
                        } else {
                            query.setParameter(getFieldNameNoKey(key), filter.get(key));
                        }
                    }
                }
            }
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> queryList(String qlString, Map<String, Object> filter,
                             int startPosition, int maxResult) {
        logger.debug("queryList(): query[" + qlString + "]");
        Query query = getEm().createQuery(qlString)
                .setFirstResult(startPosition).setMaxResults(maxResult);
        if (filter != null) {
            for (String key : filter.keySet()) {
                if (key.equals("start") || key.equals("end")) {
                    continue;
                } else {
                    if (key.equals("createTime")) {
                        if(","!=filter.get(key)&&null!=filter.get(key)&&!filter.get(key).equals(",")) {
                            String[] dv = (filter.get(key).toString()).split(",");
                            if (dv.length > 1 && null != dv[0] && !dv[0].equals("") && null != dv[1] && !dv[1].equals("")) {
                                query.setParameter(getFieldNameNoKey("start"), filter.get("start"));
                                query.setParameter(getFieldNameNoKey("end"), filter.get("end"));
                            } else {
                                if (null != dv[0] && !dv[0].equals("")) {
                                    query.setParameter(getFieldNameNoKey("start"), filter.get("start"));
                                } else if (null != dv[1] && !dv[1].equals("")) {
                                    query.setParameter(getFieldNameNoKey("end"), filter.get("end"));
                                }
                            }
                        }
                    } else {
                        query.setParameter(getFieldNameNoKey(key), filter.get(key));
                    }
                }
            }
        }
        return query.getResultList();
    }

    public int executeUpdate(String jpql, Object... params) {
        Query query = getEm().createQuery(jpql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public T querySingleBySql(String nativeSql, Object[] params) {
        Query query = getEm().createNativeQuery(nativeSql, this.entityClass);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return (T) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<T> queryListBySql(String nativeSql, Object[] params) {
        Query query = getEm().createNativeQuery(nativeSql, this.entityClass);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query.getResultList();
    }

    /**
     * 构造查询子句
     *
     * @param filter
     * @return
     */
    protected String getFilter(Map<String, Object> filter) {
        if (filter == null || filter.isEmpty()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer(" where ");
        Map<String, Object> newfilter=new HashMap<>();
        for (String fieldName : filter.keySet()) {
            Object value = filter.get(fieldName);
            if (null != value && !"".equals(value)) {
                if (fieldName.equals("start") || fieldName.equals("end")) {
                    continue;
                } else {
                    if (fieldName.equals("createTime")) {
                        if ("," != value && null != value && !value.equals(",")) {
                            String[] dv = (value.toString()).split(",");
                            if (dv.length > 1 && null != dv[0] && !dv[0].equals("") && null != dv[1] && !dv[1].equals("")) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    newfilter.put("start", sdf.parse(dv[0]));
                                    newfilter.put("end", sdf.parse(dv[1]));
                                } catch (Exception e) {
                                }
                                buffer.append(String.format("t.%s BETWEEN :%s and :%s and ", new Object[]{
                                        fieldName, getFieldNameNoKey("start"), getFieldNameNoKey("end")}));

                            } else {
                                if (null != dv[0] && !dv[0].equals("")) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        newfilter.put("start", sdf.parse(dv[0]));
                                    } catch (Exception e) {
                                    }
                                    buffer.append(String.format("t.%s >= :%s and ", new Object[]{
                                            fieldName, getFieldNameNoKey("start")}));

                                } else if (null != dv[1] && !dv[1].equals("")) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        newfilter.put("end", sdf.parse(dv[1]));
                                    } catch (Exception e) {
                                    }
                                    buffer.append(String.format("t.%s <= :%s and ", new Object[]{
                                            fieldName, getFieldNameNoKey("end")}));

                                }
                            }
                        }
                    } else {
                        if (value instanceof String) {
                            buffer.append(String.format("t.%s like :%s and ", new Object[]{
                                    fieldName, getFieldNameNoKey(fieldName)}));
                        } else {
                            buffer.append(String.format("t.%s = :%s and ", new Object[]{fieldName, getFieldNameNoKey(fieldName)}));
                        }
                    }
                }
            }
        }
        filter.putAll(newfilter);
        String result = buffer.toString();
        if(result.trim().equals("where")){
            result="";
        }else {
            result = result.substring(0, result.length() - 5);
        }
        return result;
    }

    protected String getOrder(String sortField, String sortOrder) {
        if (StringUtils.isEmpty(sortField))
            return "";
        StringBuffer buffer = new StringBuffer(" order by ");
        buffer.append(sortField);
        buffer.append(" ");
        if (StringUtils.isEmpty(sortOrder))
            buffer.append("asc");
        else
            buffer.append(sortOrder);
        return buffer.toString();
    }

    private String getFieldNameNoKey(String fieldName) {
        if (fieldName.contains(".")) {
            return fieldName.replaceAll("\\.", "\\$");
        } else
            return fieldName;
    }

}