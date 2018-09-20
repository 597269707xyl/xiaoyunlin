package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgCharsetEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgCharsetEnumDao extends JpaRepository<MsgCharsetEnum, Long>, JpaSpecificationExecutor<MsgCharsetEnum> {
    @Transactional
    @Modifying
    @Query("delete from MsgCharsetEnum e where e.charset.id = ?1")
    void delelteByCharsetId(Long id);
    /**
     * 自动生成用例值
     * @author wzx
     * @param charsetId
     * @return
     */
    @Query(value = "select c from MsgCharsetEnum c where c.charset.id=:charsetId")
    public List<MsgCharsetEnum> findByCharsetId(@Param("charsetId")Long charsetId);
}
