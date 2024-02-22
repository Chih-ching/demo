package com.example.demo.dao;

import com.example.demo.po.CollectionPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CollectionDAO extends JpaRepository<CollectionPo, Long> {

    @Query(value = " SELECT * "
            + "	     FROM collection  "
            + "      WHERE :start ::::DATE <= date AND date <= :end ::::DATE "
            + "      ORDER BY date "
            , nativeQuery = true)
    List<Map<String, Object>> dateRangeQuery(@Param("start") String start, @Param("end") String end);
}
