package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import org.dom4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SimSysInsMessageFieldService
 * @author xiaolanli
 * @date 2016/5/18
 */
@Service
public class SimSysInsMessageFieldService {
    @Autowired
    private SimSysInsMessageFieldDao simSysInsMessageFieldDao;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;
    @Autowired
    private MsgFieldSetCompDao msgFieldSetCompDao;
    @Autowired
    private MsgFieldSetDao msgFieldSetDao;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SimSystemDao simSystemDao;
    @Autowired
    private SimSysinsMsgFieldValueDao simSysinsMsgFieldValueDao;
    @Autowired
    private NxyFuncUsecaseDataDao nxyFuncUsecaseDataDao;

    public List<SimSysInsMessageField> getAllFields(Long id) {
        return simSysInsMessageFieldDao.findByid(id);
    }

    public List<SimSysInsMessageField> getSortAllFields(Long id) {
        List<SimSysInsMessageField> listHead = simSysInsMessageFieldDao.findByMsgIdAndFieldType(id,"HEAD");
        List<SimSysInsMessageField> listBody = simSysInsMessageFieldDao.findByMsgIdAndFieldType(id,"BODY");
        List<SimSysInsMessageField> listExtBody = simSysInsMessageFieldDao.findByMsgIdAndFieldType(id,"EXTENDBODY");
        listHead.addAll(listBody);
        listHead.addAll(listExtBody);

        return listHead;
    }

    public void addMessageField(Long simid, Long generalid, SimSysInsMessage simMessage) {
        SimSystemInstance simsystem=simSystemInstanceDao.findOne(simid);
        List<Long> setids=new ArrayList<>();
        List<MsgFieldSetComp> headLists = msgFieldSetCompDao.findByheadsetid(simsystem.getSimSystem().getHeadFieldSet().getId());
        SimSysInsMessage simmessage=simSysInsMessageDao.findOne(simMessage.getId());
        List<SimSysInsMessageField> simMessageField1=simSysInsMessageFieldDao.findByid(simmessage.getId());
        if(simMessageField1.size()!=0) {
            if (simsystem.getSimSystem().getMsgType().equals("SELF")) {
                MsgFieldSet msgFieldSet = msgFieldSetDao.findOne(generalid);
                if(msgFieldSet!=null) {
                    simSysInsMessageFieldDao.deleteByMsgidAndFieldType(simmessage.getId(), simsystem.getSimSystem().getHeadFieldSet().getSetType(), msgFieldSet.getSetType());
                }else{
                    simSysInsMessageFieldDao.deleteByMsgid(simmessage.getId(), simsystem.getSimSystem().getHeadFieldSet().getSetType());
                }
                } else {
                simSysInsMessageFieldDao.deleteByMsgidAndFieldType(simmessage.getId(), simsystem.getSimSystem().getHeadFieldSet().getSetType(), "XMLBODY");
            }
        }
            if(headLists!=null){
            for(MsgFieldSetComp head:headLists){
                SimSysInsMessageField  simMessageField=new SimSysInsMessageField();
                simMessageField.setSimSysInsMessage(simMessage);
                simMessageField.setFieldType(head.getFieldSet().getSetType());
                simMessageField.setMoFlag(head.isMoFlag());
                simMessageField.setFixFlag(head.isFixFlag());
                simMessageField.setMsgField(head.getField());
                simMessageField.setDefaultValue(head.getDefaultValue());
                simSysInsMessageFieldDao.save(simMessageField);
            }
        }
        if(simsystem.getSimSystem().getMsgType().equals("SELF")) {
            List<MsgFieldSetComp> bodylists = msgFieldSetCompDao.findByheadsetid(generalid);
            if (bodylists != null) {
                for (MsgFieldSetComp body : bodylists) {
                    SimSysInsMessageField simSysInsMessageField = new SimSysInsMessageField();
                    simSysInsMessageField.setSimSysInsMessage(simMessage);
                    simSysInsMessageField.setFieldType(body.getFieldSet().getSetType());
                    simSysInsMessageField.setMoFlag(body.isMoFlag());
                    simSysInsMessageField.setFixFlag(body.isFixFlag());
                    simSysInsMessageField.setMsgField(body.getField());
                    simSysInsMessageField.setDefaultValue(body.getDefaultValue());
                    simSysInsMessageFieldDao.save(simSysInsMessageField);
                }
            }
        }
        List<SimSysInsMessageField> simMessageField2=simSysInsMessageFieldDao.findByid(simmessage.getId());
        if(simMessageField2.size()!=0) {
            for (SimSysInsMessageField s : simMessageField2) {
                MsgField f = s.getMsgField();
                MsgDataType msgDataType = f.getDataType();
                List<SimSysinsMsgFieldValue> simMsgFieldValueLista = simSysinsMsgFieldValueDao.findByFid(s.getId());
                if (simMsgFieldValueLista.size() == 0) {
                    List<MsgDataTypeEnum> msgDataTypeEnumList = msgDataType.getDatas();
                    if (msgDataTypeEnumList.size() != 0) {
                        for (MsgDataTypeEnum m : msgDataTypeEnumList) {
                            SimSysinsMsgFieldValue simMsgFieldValue = new SimSysinsMsgFieldValue();
                            simMsgFieldValue.setSimMessageField(s);
                            simMsgFieldValue.setValueType(m.getDataSetType());
                            simMsgFieldValue.setValueRule(m.getGeneratRule());
                            simMsgFieldValue.setDescript(m.getDescript());
                            simSysinsMsgFieldValueDao.save(simMsgFieldValue);
                        }
                    }
                }
            }
        }
    }

    public void addFields(Long simMessageid, List<Long> list) {
        SimSysInsMessage simMessage=simSysInsMessageDao.findOne(simMessageid);
        if (simMessage != null){
            List<SimSysInsMessageField> fields = new ArrayList<>();
            for (Long id:list){
                SimSysInsMessageField field1=simSysInsMessageFieldDao.findByTwoId(id,simMessageid);
                if(field1 ==null) {
                    SimSysInsMessageField field = new SimSysInsMessageField();
                    field.setSimSysInsMessage(simMessage);
                    MsgField f = msgFieldDao.findOne(id);
                    field.setDefaultValue(f.getDefaultValue());
                    field.setMsgField(f);
                    field.setFixFlag(f.isFixFlag());
                    field.setMoFlag(f.isMoFlag());
                    if(simMessage.getType().equals("SELF")) {
                        field.setFieldType("EXTENDBODY");
                    }else{
                        field.setFieldType("XMLBODY");
                        //模板和测试用例增加域
                        Document doc = XmlDocHelper.getXmlFromStr(simMessage.getModelFileContent());
                        String fieldId = f.getFieldId().trim();
                        String value = f.getDefaultValue();
                        DocumentHelper.makeElement(doc, fieldId);
                        XmlDocHelper.setNodeValue(doc, fieldId, value);
                        String fileContent = doc.asXML();
                        simMessage.setModelFileContent(fileContent);
                        simSysInsMessageDao.save(simMessage);
                        List<NxyFuncUsecaseData> dataList = nxyFuncUsecaseDataDao.findByMessageId(simMessage.getId());
                        for(NxyFuncUsecaseData data : dataList){
                            doc = XmlDocHelper.getXmlFromStr(data.getMessageMessage());
                            DocumentHelper.makeElement(doc, fieldId);
                            XmlDocHelper.setNodeValue(doc, fieldId, value);
                            String xml = doc.asXML();
                            data.setMessageMessage(xml);
                        }
                        nxyFuncUsecaseDataDao.save(dataList);
                    }
                    simSysInsMessageFieldDao.save(field);
                   // fields.add(field);
                }
            }
          //  simMessage.getMsgFields().addAll(fields);
           // simSysInsMessageDao.save(simMessage);
            for (Long id:list) {
                SimSysInsMessageField field2 = simSysInsMessageFieldDao.findByTwoId(id, simMessageid);
                MsgField f = msgFieldDao.findOne(id);
                MsgDataType msgDataType = f.getDataType();
                List<SimSysinsMsgFieldValue> simMsgFieldValueList = simSysinsMsgFieldValueDao.findByFid(field2.getId());
                if (msgDataType != null) {
                    if (simMsgFieldValueList.size() == 0) {
                        List<MsgDataTypeEnum> msgDataTypeEnumList = msgDataType.getDatas();
                        if (msgDataTypeEnumList.size() != 0) {
                            for (MsgDataTypeEnum m : msgDataTypeEnumList) {
                                SimSysinsMsgFieldValue simMsgFieldValue = new SimSysinsMsgFieldValue();
                                simMsgFieldValue.setSimMessageField(field2);
                                simMsgFieldValue.setValueType(m.getDataSetType());
                                simMsgFieldValue.setValueRule(m.getGeneratRule());
                                simMsgFieldValue.setDescript(m.getDescript());
                                simSysinsMsgFieldValueDao.save(simMsgFieldValue);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root><a>1</a></root>";
        Document doc = XmlDocHelper.getXmlFromStr(xml);
        DocumentHelper.makeElement(doc, "/root/a/b");
        XmlDocHelper.setNodeValue(doc, "/root/a/b", "2");
        System.out.println(doc.asXML());
    }

    public void delFields(Long simMessageId, List<Long> list) {
        for (Long id:list){
            //删除域时，报文模板和测试用例同步删除该域信息。
            SimSysInsMessageField field = simSysInsMessageFieldDao.findOne(id);
            SimSysInsMessage simSysInsMessage = field.getSimSysInsMessage();
            String simMessageXml = simSysInsMessage.getModelFileContent();
            Document doc = XmlDocHelper.getXmlFromStr(simMessageXml);
            String fieldId = field.getMsgField().getFieldId();
            Node node = doc.selectSingleNode(fieldId);
            node.getParent().remove(node);
            String fileContent = doc.asXML();
            simSysInsMessage.setModelFileContent(fileContent);
            simSysInsMessageDao.save(simSysInsMessage);
            List<NxyFuncUsecaseData> dataList = nxyFuncUsecaseDataDao.findByMessageId(simSysInsMessage.getId());
            for(NxyFuncUsecaseData data : dataList){
                doc = XmlDocHelper.getXmlFromStr(data.getMessageMessage());
                node = doc.selectSingleNode(fieldId);
                node.getParent().remove(node);
                String xml = doc.asXML();
                data.setMessageMessage(xml);
                nxyFuncUsecaseDataDao.save(dataList);
            }
            //删除域信息
            simSysInsMessageFieldDao.delete(id);
        }
    }

    public void signFields(Long simMessageId, List<Long> list) {
        for (Long id:list){
            SimSysInsMessageField field = simSysInsMessageFieldDao.findOne(id);
            field.setSignFlag(true);
            simSysInsMessageFieldDao.save(field);
        }
    }
    public void addMoFlagFields(Long simMessageId, List<Long> list) {
        for (Long id:list){
            SimSysInsMessageField field = simSysInsMessageFieldDao.findOne(id);
            field.setMoFlag(field.getMoFlag()==null?true:!field.getMoFlag());
            simSysInsMessageFieldDao.save(field);
        }
    }

    public void saveFiled(Long id, SimSysInsMessageField field) {
        SimSysInsMessageField f = simSysInsMessageFieldDao.findOne(id);
        if (f != null){
            f.setFixFlag(field.getFixFlag());
            f.setDefaultValue(field.getDefaultValue());
            f.setMoFlag(field.getMoFlag());
            f.setSignFlag(field.getSignFlag());
            f.setRespValueType(field.getRespValueType());
            f.setRespValue(field.getRespValue());
            f.setEncryptFlag(field.getEncryptFlag());
            f.setSeqNo(field.getSeqNo());
            simSysInsMessageFieldDao.save(f);
        }
    }

    public SimSysInsMessageField findOne(Long id) {
        return simSysInsMessageFieldDao.findByoneid(id);
    }

    public List<SimSysInsMessageField> getJhSortAllFields(Long id) {
        return simSysInsMessageFieldDao.findJhSortFields(id);
    }
}
