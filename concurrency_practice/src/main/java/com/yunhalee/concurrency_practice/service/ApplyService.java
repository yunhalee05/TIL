package com.yunhalee.concurrency_practice.service;

import com.yunhalee.concurrency_practice.domain.Coupon;
import com.yunhalee.concurrency_practice.producer.CouponCreateProducer;
import com.yunhalee.concurrency_practice.repository.CouponCountRepository;
import com.yunhalee.concurrency_practice.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    public void apply(Long userId) {
        Long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponCreateProducer.create(userId);
    }
}
