package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgCharset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgCharsetDao extends JpaRepository<MsgCharset, Long>, JpaSpecificationExecutor<MsgCharset> {

    @Query(value = "select k from MsgCharset k where  k.name=:name")
    public MsgCharset findByName(@Param("name") String name);

    @Query(value = "select c from MsgCharset c where  c.name=:name or c.code=:code")
    public MsgCharset findByNameOrCode(@Param("name") String name, @Param("code")String code);

    /**
     * 自动生成用例值
     * @author wzx
     * @param code
     * @return
     */
    @Query(value = "select c from MsgCharset c where c.code=:code")
    public MsgCharset findByCode(@Param("code")String code);
}
