package com.zdtech.platform.service.message;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.MsgCharsetDao;
import com.zdtech.platform.framework.repository.MsgDataTypeDao;
import com.zdtech.platform.framework.repository.MsgDataTypeEnumDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zdtech.platform.framework.service.GenericService;


import java.util.*;

/**
 * Created by leepan on 2016/5/4.
 */
@Service
public class MsgDataTypeService {
    @Autowired
    private MsgDataTypeDao msgDataTypeDao;
    @Autowired
    MsgCharsetDao msgCharsetDao;
    @Autowired
    GenericService genericService;
    @Autowired
    private MsgDataTypeEnumDao msgDataTypeEnumDao;

    public Map<String,Object> getAll() {
        List<MsgDataType> list = msgDataTypeDao.findAll();
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

    public MsgDataType get(Long id) {
        return msgDataTypeDao.findOne(id);
    }

    public void delCharset(Long id) {
        MsgCharset msgCharset = msgCharsetDao.findOne(id);
        if (msgCharset != null){
            msgCharsetDao.delete(msgCharset);
        }
    }

    public Result addDataType(MsgDataType msgDataType, Long charsetId) {
        Result result = null;
        if (msgDataType.getId() == null){
            MsgDataType msgDataType1 = msgDataTypeDao.findByNameandStand(msgDataType.getName(),msgDataType.getSerial());
            if (null != msgDataType1) {
                result = new Result(false,"该数据类型名称和标准已经存在!");
            } else {
                msgDataTypeDao.save(msgDataType);
                result = new Result(true,"数据类型添加成功");
            }
        }else {
            MsgDataType fs = msgDataTypeDao.findOne(msgDataType.getId());
            fs.setName(msgDataType.getName());
            fs.setCode(msgDataType.getCode());
            fs.setDescript(msgDataType.getDescript());
            fs.setSerial(msgDataType.getSerial());
            fs.setType(msgDataType.getType());
            msgDataTypeDao.save(fs);
            result = new Result(true,"数据类型修改成功");
        }
        return result;
    }

    public void deleteDataTypes(List<Long> list) {
        for (Long id:list){
            deleteDataType(id);
        }
    }

    private void deleteDataType(Long id) {
        msgDataTypeDao.delete(id);
    }

    public Result addDataTypeEnum(MsgDataTypeEnum msgDataTypeEnum, Long dataTypeId) {
        Result result = null;
        MsgDataType msgDataType=msgDataTypeDao.findOne(dataTypeId);
        msgDataTypeEnum.setDataType(msgDataType);
        if (msgDataTypeEnum.getId() == null){
            msgDataTypeEnumDao.save(msgDataTypeEnum);
                result = new Result(true,"数据类型添加成功");
        }else {
            MsgDataTypeEnum fs = msgDataTypeEnumDao.findOne(msgDataTypeEnum.getId());
            fs.setDataType(msgDataType);
            fs.setDataSetType(msgDataTypeEnum.getDataSetType());
            fs.setGeneratRule(msgDataTypeEnum.getGeneratRule());
            msgDataTypeEnumDao.save(fs);
            result = new Result(true,"数据类型修改成功");
        }
        return result;
    }

    public void delDataTypeEnum(Long dataTypeId, List<Long> list) {
        for (Long id:list){
            msgDataTypeEnumDao.delete(id);
        }
    }

    public MsgDataTypeEnum getDataEnum(Long id) {
        return msgDataTypeEnumDao.findOne(id);
    }

    public Result updateDataTypeEnum(MsgDataTypeEnum msgDataTypeEnum, Long id) {
        Result result =null;
        MsgDataTypeEnum fs = msgDataTypeEnumDao.findOne(msgDataTypeEnum.getId());
        fs.setDataSetType(msgDataTypeEnum.getDataSetType());
        fs.setGeneratRule(msgDataTypeEnum.getGeneratRule());
        fs.setDescript(msgDataTypeEnum.getDescript());
        msgDataTypeEnumDao.save(fs);
        result = new Result(true,"数据类型修改成功");
        return result;
    }

/*    public Map<String,Object> findMsgCharsetEnum(Map<String, Object> params, Pageable page) {
        Pageable p = page;
        Map<String,Object> map = genericService.commonQuery("msgCharset",params,p);
        List<MsgCharset> rows = (List<MsgCharset>)map.get("rows");
        List<Map<String ,Object>> transRows = new ArrayList<>();
        for (MsgCharset temp1 : rows){
            try{
                Map<String,Object> transRow = BeanUtils.describe(temp1);
                if (temp1.getChars() != null && temp1.getChars().size() > 0){
                    StringBuilder tempEnum = new StringBuilder();
                    for (MsgCharsetEnum temp2 : temp1.getChars()){
                        tempEnum.append(temp2.getValue());
                        tempEnum.append(",");
                    }
                    if (tempEnum.charAt(tempEnum.length()-1) == ','){
                        tempEnum.deleteCharAt(tempEnum.length()-1);
                    }
                    transRow.put("enum",tempEnum.toString());
                }
                transRows.add(transRow);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("rows",transRows);
        return map;
    }*/

}
