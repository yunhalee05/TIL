package com.yunhalee.concurrency_practice_consumer.repository;

import com.yunhalee.concurrency_practice_consumer.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
