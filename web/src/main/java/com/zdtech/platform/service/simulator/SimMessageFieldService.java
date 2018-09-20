package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SimMessageFieldService
 *
 * @author xiaolanli
 * @date 2016/5/11.
 */
@Service
public class SimMessageFieldService {
    @Autowired
    private SimMessageDao simMessageDao;
    @Autowired
    private SimSystemDao simSystemDao;
    @Autowired
    private MsgFieldSetCompDao msgFieldSetCompDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SimMessageFieldDao simMessageFieldDao;
    @Autowired
    private MsgFieldSetDao msgFieldSetDao;
    @Autowired
    private MsgDataTypeEnumDao msgDataTypeEnumDao;
    @Autowired
    private SimMsgFieldValueDao simMsgFieldValueDao;

    public void addfield(Long simid, Long generalid, SimMessage simMessage) {
        SimSystem simsystem=simSystemDao.findByid(simid);
        List<MsgFieldSetComp> headlists = new ArrayList<>();//msgFieldSetCompDao.findByheadsetid(simsystem.getHeadFieldSet().getId());
        SimMessage simmessage=simMessageDao.findOne(simMessage.getId());
        List<SimMessageField> simMessageField1=simMessageFieldDao.findByid(simmessage.getId());
        if(simMessageField1.size()!=0) {
            if (simsystem.getMsgType().equals("SELF")) {
                MsgFieldSet msgFieldSet = msgFieldSetDao.findOne(generalid);
                if(msgFieldSet!=null) {
                    simMessageFieldDao.deleteByMsgidAndFieldType(simmessage.getId(), simsystem.getHeadFieldSet().getSetType(), msgFieldSet.getSetType());
                }else{
                    simMessageFieldDao.deleteByMsgid(simmessage.getId(), simsystem.getHeadFieldSet().getSetType());
                }
                } else {
                simMessageFieldDao.deleteByMsgidAndFieldType(simmessage.getId(), simsystem.getHeadFieldSet().getSetType(), "XMLBODY");
            }
        }
        if(headlists.size()!=0){
            for(MsgFieldSetComp head:headlists){
                SimMessageField  simMessageField=new SimMessageField();
                simMessageField.setSimMessage(simmessage);
                simMessageField.setFieldType(head.getFieldSet().getSetType());
                simMessageField.setMflag(head.isMoFlag());
                simMessageField.setFflag(head.isFixFlag());
                simMessageField.setMsgField(head.getField());
                simMessageField.setDvalue(head.getDefaultValue());
                simMessageFieldDao.save(simMessageField);
            }
       }
        if(simsystem.getMsgType().equals("SELF")) {
            List<MsgFieldSetComp> bodylists = msgFieldSetCompDao.findByheadsetid(generalid);
            if(bodylists!=null){
                for(MsgFieldSetComp body:bodylists){
                    SimMessageField  simMessageField=new SimMessageField();
                    simMessageField.setSimMessage(simmessage);
                    simMessageField.setFieldType(body.getFieldSet().getSetType());
                    simMessageField.setMflag(body.isMoFlag());
                    simMessageField.setFflag(body.isFixFlag());
                    simMessageField.setMsgField(body.getField());
                    simMessageField.setDvalue(body.getDefaultValue());
                    simMessageFieldDao.save(simMessageField);
                }
            }
        }
        List<SimMessageField> simMessageField2=simMessageFieldDao.findByid(simmessage.getId());
        if(simMessageField2.size()!=0) {
            for (SimMessageField s : simMessageField2) {
                MsgField f = s.getMsgField();
                MsgDataType msgDataType = f.getDataType();
                List<SimMsgFieldValue> simMsgFieldValueLista = simMsgFieldValueDao.findByFid(s.getId());
                if (simMsgFieldValueLista.size() == 0) {
                    List<MsgDataTypeEnum> msgDataTypeEnumList = msgDataType.getDatas();
                    if (msgDataTypeEnumList.size() != 0) {
                        for (MsgDataTypeEnum m : msgDataTypeEnumList) {
                            SimMsgFieldValue simMsgFieldValue = new SimMsgFieldValue();
                            simMsgFieldValue.setSimMessageField(s);
                            simMsgFieldValue.setValueType(m.getDataSetType());
                            simMsgFieldValue.setValueRule(m.getGeneratRule());
                            simMsgFieldValue.setDescript(m.getDescript());
                            simMsgFieldValueDao.save(simMsgFieldValue);
                        }
                    }
                }
            }
        }
    }

    public List<SimMessageField> getall(Long id){
        return simMessageFieldDao.findByid(id);
    }

    public void addFields(Long simMessageid, List<Long> fieldlist, ArrayList<Leaf> elemList) {
        SimMessage simMessage=simMessageDao.findOne(simMessageid);
        if (simMessage != null){
            List<SimMessageField> fields = new ArrayList<>();
            int i=0;
            for (Long id:fieldlist){
                SimMessageField field1=simMessageFieldDao.findByTwoId(id,simMessageid);
                if(field1 ==null) {
                    SimMessageField field = new SimMessageField();
                    field.setSimMessage(simMessage);
                    MsgField f = msgFieldDao.findOne(id);
                    if(elemList != null){
                        field.setDvalue(elemList.get(i).getValue());
                    } else {
                        field.setDvalue(f.getDefaultValue());
                    }
                    field.setMsgField(f);
                    field.setSeq_no(i+1);
                    field.setFflag(f.isFixFlag());
                    field.setMflag(f.isMoFlag());
                        if(simMessage.getType().equals("SELF")) {
                            field.setFieldType("EXTENDBODY");
                        }else{
                            field.setFieldType("XMLBODY");
                        }
                    fields.add(field);
                } else {
                    if(elemList != null){
                        field1.setDvalue(elemList.get(i).getValue());
                    }
                    field1.setSeq_no(i+1);
                    simMessageFieldDao.save(field1);
                }
                i++;
            }
            simMessage.getSimMessageFields().addAll(fields);
            simMessageDao.save(simMessage);
            for (Long id:fieldlist) {
                SimMessageField field2 = simMessageFieldDao.findByTwoId(id, simMessageid);
                MsgField f = msgFieldDao.findOne(id);
                MsgDataType msgDataType = f.getDataType();
                if (msgDataType!= null) {
                    List<SimMsgFieldValue> simMsgFieldValueList = simMsgFieldValueDao.findByFid(field2.getId());
                    if (simMsgFieldValueList.size() == 0) {
                        List<MsgDataTypeEnum> msgDataTypeEnumList = msgDataType.getDatas();
                        if (msgDataTypeEnumList.size() != 0) {
                            for (MsgDataTypeEnum m : msgDataTypeEnumList) {
                                SimMsgFieldValue simMsgFieldValue = new SimMsgFieldValue();
                                simMsgFieldValue.setSimMessageField(field2);
                                simMsgFieldValue.setValueType(m.getDataSetType());
                                simMsgFieldValue.setValueRule(m.getGeneratRule());
                                simMsgFieldValue.setDescript(m.getDescript());
                                simMsgFieldValueDao.save(simMsgFieldValue);
                            }
                        }
                    }
                }
            }
        }
    }

    public void delFields(Long simMessageId, List<Long> list) {
        for (Long id:list){
            simMessageFieldDao.delete(id);
        }
    }

    public void signFields(Long simMessageId, List<Long> list) {
        for (Long id:list){
            SimMessageField field = simMessageFieldDao.findOne(id);
            field.setSflag(true);
            simMessageFieldDao.save(field);
        }
    }

    public void saveFiled(Long id, SimMessageField field) {
        SimMessageField f = simMessageFieldDao.findOne(id);
        if (f != null){
            f.setFflag(field.getFflag());
            f.setDvalue(field.getDvalue());
            f.setMflag(field.getMflag());
            f.setSeq_no(field.getSeq_no());
            f.setSflag(field.getSflag());
            f.setValueType(field.getValueType());
            f.setValue(field.getValue());
            f.setSeq_no(field.getSeq_no());
            simMessageFieldDao.save(f);
        }
    }

    public SimMessageField findOne(Long id) {
        return simMessageFieldDao.findByoneid(id);
    }

}
