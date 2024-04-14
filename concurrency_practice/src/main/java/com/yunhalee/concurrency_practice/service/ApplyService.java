package com.yunhalee.concurrency_practice.service;

import com.yunhalee.concurrency_practice.domain.Coupon;
import com.yunhalee.concurrency_practice.repository.CouponCountRepository;
import com.yunhalee.concurrency_practice.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }

    public void apply(Long userId) {
        Long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));

    }
}
