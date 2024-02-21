package com.example.demo.dao;

import com.example.demo.po.CollectionPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionDAO extends JpaRepository<CollectionPo, Long> {

}
