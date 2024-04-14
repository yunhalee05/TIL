package com.yunhalee.concurrency_practice.repository;

import com.yunhalee.concurrency_practice.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
