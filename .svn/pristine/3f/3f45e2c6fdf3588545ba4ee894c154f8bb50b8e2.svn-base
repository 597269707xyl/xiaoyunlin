package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SysConf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by htma on 2016/5/17.
 */
public interface SysConfDao extends JpaRepository<SysConf,Long>,JpaSpecificationExecutor<SysConf> {

    int countByKey(String key);

    int countByCategoryAndValue(String category, String value);

    List<SysConf> findByCategory(String category);

    Long countByCategory(String category);

    SysConf findByCategoryAndKey(String category,String key);

}
