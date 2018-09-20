package com.zdtech.platform.framework.repository;


import com.zdtech.platform.framework.entity.Seq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeqDao extends JpaRepository<Seq, String>
{
}