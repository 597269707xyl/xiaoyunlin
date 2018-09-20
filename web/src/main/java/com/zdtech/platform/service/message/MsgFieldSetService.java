package com.zdtech.platform.service.message;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.MsgFieldSet;
import com.zdtech.platform.framework.entity.MsgFieldSetComp;
import com.zdtech.platform.framework.repository.MsgFieldDao;
import com.zdtech.platform.framework.repository.MsgFieldSetCompDao;
import com.zdtech.platform.framework.repository.MsgFieldSetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leepan on 2016/5/5.
 */
@Service
public class MsgFieldSetService {
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private MsgFieldSetDao msgFieldSetDao;
    @Autowired
    private MsgFieldSetCompDao msgFieldSetCompDao;

    public List<MsgField> getFieldsByIds(List<Long> ids){
        return msgFieldDao.getFieldsByIds(ids);
        /*List<MsgField> ret = new ArrayList<>();
        if (ids == null || ids.size() < 1){
            return ret;
        }
        for (Long id:ids){
            ret.add(msgFieldDao.findOne(id));
        }
        return ret;*/
    }

    public MsgFieldSet get(Long id) {
        return msgFieldSetDao.findOne(id);
    }

    public void addFieldSet(MsgFieldSet fieldSet) {
        if (fieldSet.getId() == null){
            msgFieldSetDao.save(fieldSet);
        }else {
            MsgFieldSet fs = msgFieldSetDao.findOne(fieldSet.getId());
            fieldSet.setFields(fs.getFields());
            msgFieldSetDao.save(fieldSet);
        }
    }
    public Map<String,Object> getAllBySetType(String setType) {
        List<MsgFieldSet> list = msgFieldSetDao.findBySetType(setType);
        Map<String,Object> ret = new HashMap<>();
        if (list == null || list.size() < 1){
            ret.put("total",0);
            ret.put("rows",list);
            return ret;
        }
        ret.put("total",list.size());
        ret.put("rows",list);
        return ret;
    }
    public void deleteFieldSets(List<Long> list) {
        for (Long id:list){
            deleteFieldSet(id);
        }
    }
    public void deleteFieldSet(Long id){
        msgFieldSetDao.delete(id);
    }

    public List<String> addFields(Long fieldSetId, List<Long> fieldList) {
        MsgFieldSet fieldSet = msgFieldSetDao.findOne(fieldSetId);
        List<String> list=new ArrayList<>();
        if (fieldSet != null){
            List<MsgFieldSetComp> fields = new ArrayList<>();
            for (Long id:fieldList) {
                MsgFieldSetComp msgField = msgFieldSetCompDao.findBySIDAndFID(id, fieldSetId);
                if (msgField == null) {
                    MsgFieldSetComp field = new MsgFieldSetComp();
                    field.setFieldSet(fieldSet);
                    MsgField f = msgFieldDao.findOne(id);
                    field.setDefaultValue(f.getDefaultValue());
                    field.setField(f);
                    field.setFixFlag(f.isFixFlag());
                    field.setMoFlag(f.isMoFlag());
                    fields.add(field);
                }else{
                    list.add(msgField.getField().getFieldId());
                }
            }
            fieldSet.getFields().addAll(fields);
            msgFieldSetDao.save(fieldSet);
        }
        return list;
    }

    public void delFields(Long fieldSetId,List<Long> list) {
        for (Long id:list){
            msgFieldSetCompDao.delete(id);
        }
        /*MsgFieldSet fieldSet = msgFieldSetDao.findOne(fieldSetId);
        if (fieldSet != null){
            for (Long id:list){
                MsgFieldSetComp fsc = msgFieldSetCompDao.findOne(id);
                fieldSet.getFields().remove(fsc);
            }
            msgFieldSetDao.save(fieldSet);
        }*/
    }


    public void saveFiled(Long id, MsgFieldSetComp field) {
        MsgFieldSetComp f = msgFieldSetCompDao.findOne(id);
        if (f != null){
            f.setFixFlag(field.isFixFlag());
            f.setDefaultValue(field.getDefaultValue());
            f.setMoFlag(field.isMoFlag());
            f.setSeqNo(field.getSeqNo());
            msgFieldSetCompDao.save(f);
        }
    }

    public List<MsgFieldSet> getallgeneralset(String type) {
        return msgFieldSetDao.findBySetType(type);
    }
}
