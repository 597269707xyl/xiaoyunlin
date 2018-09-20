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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2018/1/16.
 */
@Service
@Transactional
public class FuncEpccService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    public Map<String, Object> queryList(Map<String, Object> params, Pageable page) {
        String sql = "select send.id,send.usecase_exec_id,send.message_name,send.message_code,send.epcc_ids,send.msg," +
                "date_format(recv.create_time, '%Y-%m-%d %H:%i:%s') as time_stamp,recv.case_no,recv.bank_no,msg_name,recv.msg_code,recv.message" +
                " from nxy_func_usecase_exec_send send JOIN nxy_func_case_recv recv ON send.epcc_ids = recv.epcc_ids where send.epcc_ids != '' ";
        String countSql = "select count(1) from nxy_func_usecase_exec_send send JOIN nxy_func_case_recv recv ON send.epcc_ids = recv.epcc_ids where send.epcc_ids != '' ";
        if (!StringUtils.isEmpty(params.get("caseNo"))) {
            sql += " and recv.case_no like '%" + params.get("caseNo") + "%'";
            countSql += " and recv.case_no like '%" + params.get("caseNo") + "%'";
        }
        if (!StringUtils.isEmpty(params.get("epccIds"))) {
            sql += " and send.epcc_ids like '%" + params.get("epccIds") + "%'";
            countSql += " and send.epcc_ids like '%" + params.get("epccIds") + "%'";
        }
        if(!StringUtils.isEmpty(params.get("beginTime"))){
            sql += " and recv.create_time>= '" + params.get("beginTime") + "'";
            countSql += " and recv.create_time>= '" + params.get("beginTime") + "'";
        }
        if(!StringUtils.isEmpty(params.get("endTime"))){
            sql += " and recv.create_time<= '" + params.get("endTime") + "'";
            countSql += " and recv.create_time<= '" + params.get("endTime") + "'";
        }
        sql += " ORDER BY send.id desc ";
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
