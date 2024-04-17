package com.yunhalee.concurrency_practice_consumer.consumer;

import com.yunhalee.concurrency_practice_consumer.domain.Coupon;
import com.yunhalee.concurrency_practice_consumer.domain.FailedEvent;
import com.yunhalee.concurrency_practice_consumer.repository.CouponRepository;
import com.yunhalee.concurrency_practice_consumer.repository.FailedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CreateCouponConsumer {

    private final CouponRepository couponRepository;

    private final FailedEventRepository failedEventRepository;

    private final Logger logger = LoggerFactory.getLogger(CreateCouponConsumer.class);

    public CreateCouponConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
        this.couponRepository = couponRepository;
        this.failedEventRepository = failedEventRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {

        try {
            couponRepository.save(new Coupon(userId));
        } catch (Exception e) {
            logger.error("쿠폰 발급에 실패하여습니다. 사용자 아이디 : " + userId);
            failedEventRepository.save(new FailedEvent(userId));
        }
    }
}
