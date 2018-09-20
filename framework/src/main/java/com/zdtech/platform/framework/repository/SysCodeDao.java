package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SysCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by lcheng on 2015/5/13.
 */
public interface SysCodeDao extends JpaRepository<SysCode,Long>,JpaSpecificationExecutor<SysCode> {

    int countByKey(String key);

    int countByCategoryAndValue(String category, String value);

    List<SysCode> findByCategoryOrderBySeqNoAscKeyAsc(String category);

    List<SysCode> findByKey(String key);

    List<SysCode> findByValue(String value);

    Page<SysCode> findByCategoryOrderBySeqNoAscKeyAsc(String category, Pageable page);

    Long countByCategory(String category);
    SysCode findByCategoryAndKey(String category, String key);
    SysCode findByCategoryAndValue(String category, String value);
}
