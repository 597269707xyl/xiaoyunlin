package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgFieldDao extends JpaRepository<MsgField, Long>, JpaSpecificationExecutor<MsgField> {
    @Query(value = "select t from MsgField t where t.id in (?1)")
    List<MsgField> getFieldsByIds(List<Long> ids);
    @Query(value="select t from MsgField t where t.fieldId=?1")
    MsgField findByFieldId(String fieldID);
    @Query(value = "select t from MsgField t where t.fieldId = ?1 and t.msgType = ?2")
    List<MsgField> findByFieldIdAndType(String fieldId,String type);
    @Query(value = "select t.id from MsgField t where t.fieldId = ?1 and t.nameEn = ?2")
    Long findIdByFiledIdAndNameEn(String filedId,String nameEn);
    @Query(value="select t.length from MsgField t where t.fieldId=?1")
    Integer findByFId(String fieldID);
    @Query(value="select t.id from MsgField t where t.fieldId=?1")
    Long findByFldId(String fieldID);

    @Query(value = "select t from MsgField t where t.fieldId = ?1")
    List<MsgField> findFieldsByFieldsId(String fieldId);

    @Query(value = "select t from MsgField t where t.msgType = ?1 and t.fieldType = ?2 order by t.fieldId asc ")
    List<MsgField> findSelfFieldsByType(String msgType,String fieldType);
}
