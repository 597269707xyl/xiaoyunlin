package com.zdtech.platform.service.funcexec;

import com.zdtech.platform.framework.entity.NxyFuncConfig;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.NxyFuncConfigDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yjli on 2017/10/12.
 */
@Service
public class FuncConfigService {
    private Logger logger = LoggerFactory.getLogger(FuncConfigService.class);

    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    @Autowired
    private EntityManager entityManager;

    public Map<String, Object> query(Map<String, Object> m, Pageable page){
        Map<String, Object> ret = new HashMap<>();
        String sql = "select config.*,i.name,t.name as projectName from nxy_func_config config left join nxy_func_item i on config.item_id = i.id left join test_project t on i.project=t.id where 1=1 ";
        if(m != null && !m.isEmpty()){
            String itemIdStr = (String) m.get("itemId");
            if(itemIdStr != null){
                sql += " and config.item_id=" + itemIdStr;
            }
            String name = (String) m.get("name");
            if(!StringUtils.isEmpty(name)){
                sql += " and (config.variable_zh like '%"+name+"%' or config.variable_en like '%"+name+"%')";
            }
        }
        Query query;
        query = entityManager.createNativeQuery(sql + " order by config.id DESC ");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1;
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber)*pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list= query.getResultList();
        }
        sql = sql.replaceFirst("select config.\\*,i.name,t.name as projectName","select count(1)");
        query = entityManager.createNativeQuery(sql);
        Object o = query.getSingleResult();
        String total = o.toString();
        ret.put("rows", list);
        ret.put("total",total);
        return ret;
    }

    public List getItems(){
        String sql = "SELECT t.* FROM nxy_func_item t LEFT JOIN nxy_func_config g ON t.id = g.item_id WHERE t.parent_id IS NULL AND g.id IS NULL AND project IS NOT NULL";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List getAll(){
        String sql = "SELECT t.* FROM nxy_func_item t WHERE t.parent_id IS NULL AND project IS NOT NULL";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = query.getResultList();
        Map<String, Object> map = new HashMap<>();
        map.put("id", -1);
        map.put("name", "公共属性");
        list.add(map);
        return list;
    }

    public Result copyConfig(Long[] configIds, Long id){
        Result ret = new Result();
        StringBuffer sb = new StringBuffer("");
        for(Long configId : configIds){
            NxyFuncConfig config = nxyFuncConfigDao.findOne(configId);
            if(!config.getId().equals(id)){
                Integer count = nxyFuncConfigDao.findByVariableEnCount(config.getVariableEn(), id);
                if(count == 0){
                    NxyFuncConfig funcConfig = new NxyFuncConfig();
                    funcConfig.setVariableEn(config.getVariableEn());
                    funcConfig.setVariableZh(config.getVariableZh());
                    funcConfig.setVariableValue(config.getVariableValue());
                    funcConfig.setCreateTime(new Date());
                    funcConfig.setItemId(id);
                    nxyFuncConfigDao.save(funcConfig);
                } else {
                    sb.append(config.getVariableEn()+',');
                }
            }
        }
        String msg = sb.toString();
        if(!msg.isEmpty()){
            ret.setSuccess(false);
            msg = msg.substring(0, msg.length()-1) + "标识已存在!";
        } else {
            msg = "复制成功!";
        }
        ret.setMsg(msg);
        return ret;
    }

    public List<NxyFuncConfig> getByItemId(Long itemId){
        return nxyFuncConfigDao.findByItemId(itemId);
    }
}
