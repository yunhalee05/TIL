package com.yunhalee.spring_db_practice.repository;

import com.yunhalee.spring_db_practice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {

}
