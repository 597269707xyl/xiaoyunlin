package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.Func;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
public interface FuncDao extends JpaRepository<Func, Long>, JpaSpecificationExecutor<Func> {

    List<Func> findByParentOrderBySeqNoAsc(Long pid);

    List<Func> findByParentOrderByLevelAscSeqNoAsc(Long uid);

    Page<Func> findByParent(Long pid, Pageable page);

    List<Func> findByParentAndLeafFalseOrderBySeqNoAsc(Long pid);

    Page<Func> findByParentAndLeafTrueOrderBySeqNoAsc(Long pid, Pageable page);

    List<Func> findByLevelOrderBySeqNoAsc(int level);

    List<Func> findByParentAndName(Long parent, String name);

    List<Func> findByUrl(String url);

    List<Func> findByName(String name);

    @Query(value = "update Func f set f.seqNo = f.seqNo+5 where f.parent = ?1 and f.seqNo>= ?2 and f.seqNo <?3")
    @Modifying
    void updateSeqNoPre(Long parent, Integer targetSeqNo, Integer srcSeqNo);

    @Query(value = "update Func f set f.seqNo = f.seqNo+5 where f.parent = ?1 and f.seqNo>= ?2")
    @Modifying
    void updateSeqNoPre(Long parent, Integer targetSeqNo);

    @Query(value = "update Func f set f.seqNo = f.seqNo+5 where f.parent = ?1 and f.seqNo> ?2 and f.seqNo <?3")
    @Modifying
    void updateSeqNoNext(Long parent, Integer targetSeqNo, Integer srcSeqNo);

    @Query(value = "update Func f set f.seqNo = f.seqNo+5 where f.parent = ?1 and f.seqNo> ?2")
    @Modifying
    void updateSeqNoNext(Long parent, Integer targetSeqNo);

    @Query(value = "update Func f set f.name=?2, f.url=?3 ,f.seqNo=?4 ,f.iconCls=?5 where f.id =?1")
    @Modifying
    void updateFuncInfo(Long id, String name, String url, Integer seqNo, String iconCls);

    @Query(value = "select k from Func k where  k.name=:name and k.level=:level and k.parent=:parent")
    Func findByName(@Param("name") String name, @Param("level") Integer level, @Param("parent") Long parent);

    @Query(value = "select k from Func k where  k.name=:name and k.level=:level and k.parent is null")
    Func findByName(@Param("name") String name, @Param("level") Integer level);

    @Query(value = "select count(k.id) from Func k where k.level =?1 and k.name=?2 and k.id !=?3")
    int countByNameNotId(Integer level, String name, Long fid);

    @Query(value = "select count(k.id) from Func k where  k.parent =?1 and k.level=?2 and k.name =?3 and k.id !=?4")
    int countByNameNotId(Long parent, Integer level, String name, Long fid);

    @Query(value = "select f from Func f where f.parent is null and f.level = 0 order by f.seqNo asc ")
    List<Func> findFirstLevel();

    @Query(value = "select count(f.id) from Func f where f.parent = ?1")
    int countOfChildren(Long id);
}
