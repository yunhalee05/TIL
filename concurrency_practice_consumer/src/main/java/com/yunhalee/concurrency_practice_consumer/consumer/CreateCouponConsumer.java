package com.yunhalee.concurrency_practice_consumer.consumer;

import com.yunhalee.concurrency_practice_consumer.domain.Coupon;
import com.yunhalee.concurrency_practice_consumer.repository.CouponRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CreateCouponConsumer {

    private final CouponRepository couponRepository;

    public CreateCouponConsumer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
        couponRepository.save(new Coupon(userId));
    }
}
