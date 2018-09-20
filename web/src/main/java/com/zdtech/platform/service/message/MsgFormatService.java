package com.zdtech.platform.service.message;

import com.zdtech.platform.framework.entity.MsgFormat;
import com.zdtech.platform.framework.entity.MsgFormatComp;
import com.zdtech.platform.framework.entity.MsgFormatCompField;
import com.zdtech.platform.framework.repository.MsgFormatCompDao;
import com.zdtech.platform.framework.repository.MsgFormatCompFieldDao;
import com.zdtech.platform.framework.repository.MsgFormatDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htma on 2016/5/9.
 */
@Service
public class MsgFormatService {
    @Autowired
    private MsgFormatDao msgFormatDao;
    @Autowired
    private MsgFormatCompDao msgFormatCompDao;
    @Autowired
    private MsgFormatCompFieldDao msgFormatCompFieldDao;


    public Map<String,Object> getAll() {
        List<MsgFormat> list = msgFormatDao.findAll();
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

    public MsgFormat get(Long id) {
        return msgFormatDao.findOne(id);
    }

    public void addMsgFormat(MsgFormat format){
        if (format.getId() == null){
            msgFormatDao.save(format);
        }else {
            MsgFormat f = get(format.getId());
            if (f != null){
                f.setDescript(format.getDescript());
                f.setName(format.getName());
                f.setType(format.getType());
                msgFormatDao.save(f);
            }
        }
    }

    public void deleteFormats(List<Long> list) {
        for (Long id:list){
            deleteFormat(id);
        }
    }

    private void deleteFormat(Long id) {
        msgFormatDao.delete(id);
    }

    public void addComp(Long formatId, MsgFormatComp comp, String protocol) {
        MsgFormat format = get(formatId);
        if (format == null){
            return;
        }
        if (comp.getId() == null){
            List<MsgFormatCompField> fields = new ArrayList<>();
            if (StringUtils.isNotEmpty(protocol)){
                String[] arr = protocol.split(",");
                for (int i = 0; i < arr.length; i++){
                    MsgFormatCompField field = new MsgFormatCompField();
                    field.setSeqNo(i+1);
                    field.setFormatComp(comp);
                    field.setProperty(arr[i]);
                    fields.add(field);
                }
            }
            comp.setMsgFormat(format);
            comp.setFields(fields);
            msgFormatCompDao.save(comp);
        }else {
            msgFormatCompDao.delete(comp.getId());
            MsgFormatComp comNew = new MsgFormatComp();
            comNew.setSeqNo(comp.getSeqNo());
            comNew.setTypeComp(comp.getTypeComp());
            comNew.setMsgFormat(format);
            List<MsgFormatCompField> fields = new ArrayList<>();
            if (StringUtils.isNotEmpty(protocol)){
                String[] arr = protocol.split(",");
                for (int i = 0; i < arr.length; i++){
                    MsgFormatCompField field = new MsgFormatCompField();
                    field.setSeqNo(i+1);
                    field.setFormatComp(comNew);
                    field.setProperty(arr[i]);
                    fields.add(field);
                }
            }
            comNew.setFields(fields);
            msgFormatCompDao.save(comNew);
        }


    }

    public void delComps(Long formatId, List<Long> list) {
        for (Long id:list){
            msgFormatCompDao.delete(id);
        }
    }

    public List<MsgFormatComp> getFormatComps(Long id) {
        return msgFormatCompDao.findFormatCompsById(id);
    }
}
