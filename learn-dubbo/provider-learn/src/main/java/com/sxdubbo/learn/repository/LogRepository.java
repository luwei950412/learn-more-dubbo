package com.sxdubbo.learn.repository;

import com.sxdubbo.learn.domain.LogPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogPO,Integer> {


}
