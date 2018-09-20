package com.zdtech.platform.service.funcexec;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2018/1/14.
 */
@Service
@Transactional
public class FuncMarkService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    public Map<String, Object> bankList(Map<String, Object> params, Pageable page){
        String sql = "select bank_no, count(case_no) as count from nxy_func_case_bank where 1=1 ";
        String countSql = "select count(bank_no) from nxy_func_case_bank where 1=1 ";
        if(!StringUtils.isEmpty(params.get("bankNo"))){
            sql += " and bank_no like '%" + params.get("bankNo") + "%'";
            countSql += " and bank_no like '%" + params.get("bankNo") + "%'";
        }
        sql += " group by bank_no ORDER BY id desc ";
        countSql += " group by bank_no ";
        countSql = "SELECT count(1) from (" + countSql + ") a";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }

        Map<String, Object> result = new HashMap();
        result.put("rows", list);

        query = entityManager.createNativeQuery(countSql);
        BigInteger count = (BigInteger) query.getSingleResult();
        result.put("total", count.longValue());
        return result;
    }

    public Map<String, Object> caseList(Map<String, Object> params, Pageable page){
        String sql = "select id, case_no, bank_no, count(bank_no) as count from nxy_func_case_recv where 1=1 ";
        String countSql = "select case_no from nxy_func_case_recv where 1=1 ";
        if(!StringUtils.isEmpty(params.get("bankNo"))){
            sql += " and bank_no = '" + params.get("bankNo") + "'";
            countSql += " and bank_no = '" + params.get("bankNo") + "'";
        }
        if(!StringUtils.isEmpty(params.get("caseNo"))){
            sql += " and case_no like '%" + params.get("caseNo") + "%'";
            countSql += " and case_no like '%" + params.get("caseNo") + "%'";
        }
        if(!StringUtils.isEmpty(params.get("beginTime"))){
            sql += " and create_time>= '" + params.get("beginTime") + "'";
            countSql += " and create_time>= '" + params.get("beginTime") + "'";
        }
        if(!StringUtils.isEmpty(params.get("endTime"))){
            sql += " and create_time<= '" + params.get("endTime") + "'";
            countSql += " and create_time<= '" + params.get("endTime") + "'";
        }
        sql += " group by case_no ORDER BY id desc ";
        countSql += " group by case_no ";
        countSql = "SELECT count(1) from (" + countSql + ") a";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }

        Map<String, Object> result = new HashMap();
        result.put("rows", list);

        query = entityManager.createNativeQuery(countSql);
        BigInteger count = (BigInteger) query.getSingleResult();
        result.put("total", count.longValue());
        return result;
    }

    public Map<String, Object> caseRecvList(Map<String, Object> params, Pageable page){
        String sql = "select id, case_no as caseNo, bank_no as bankNo, msg_seq_no as msgSeqNo, msg_code as msgCode, msg_name as msgName, date_format(create_time, '%Y-%m-%d %H:%i:%s') as createTime " +
                "from nxy_func_case_recv where 1=1 ";
        String countSql = "select count(1) from nxy_func_case_recv where 1=1 ";
        if(!StringUtils.isEmpty(params.get("bankNo"))){
            sql += " and bank_no = '" + params.get("bankNo") + "'";
            countSql += " and bank_no = '" + params.get("bankNo") + "'";
        }
        if(!StringUtils.isEmpty(params.get("caseNo"))){
            sql += " and case_no = '" + params.get("caseNo") + "'";
            countSql += " and case_no = '" + params.get("caseNo") + "'";
        }
        if(!StringUtils.isEmpty(params.get("msgSeqNo"))){
            sql += " and msg_seq_no like '%" + params.get("msgSeqNo") + "%'";
            countSql += " and msg_seq_no like '%" + params.get("msgSeqNo") + "%'";
        }
        if(!StringUtils.isEmpty(params.get("msgCode"))){
            sql += " and msg_code like '%" + params.get("msgCode") + "%'";
            countSql += " and msg_code like '%" + params.get("msgCode") + "%'";
        }
        if(!StringUtils.isEmpty(params.get("beginTime"))){
            sql += " and create_time>= '" + params.get("beginTime") + "'";
            countSql += " and create_time>= '" + params.get("beginTime") + "'";
        }
        if(!StringUtils.isEmpty(params.get("endTime"))){
            sql += " and create_time<= '" + params.get("endTime") + "'";
            countSql += " and create_time<= '" + params.get("endTime") + "'";
        }
        sql += " ORDER BY id desc ";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }

        Map<String, Object> result = new HashMap();
        result.put("rows", list);

        query = entityManager.createNativeQuery(countSql);
        BigInteger count = (BigInteger) query.getSingleResult();
        result.put("total", count.longValue());
        return result;
    }
}
