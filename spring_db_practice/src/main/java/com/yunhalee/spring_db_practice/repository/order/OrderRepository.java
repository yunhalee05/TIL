package com.yunhalee.spring_db_practice.repository.order;

import com.yunhalee.spring_db_practice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
