package com.yunhalee.concurrency_practice_consumer.repository;

import com.yunhalee.concurrency_practice_consumer.domain.Coupon;
import com.yunhalee.concurrency_practice_consumer.domain.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {

}
