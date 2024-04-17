package com.yunhalee.concurrency_practice.service;

import com.yunhalee.concurrency_practice.repository.CouponCountRepository;
import com.yunhalee.concurrency_practice.repository.CouponRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponCountRepository couponCountRepository;

    @AfterEach
    public void clear() {
        couponCountRepository.clear();
    }

    @Test
    public void 한번만응모() {
        applyService.apply(1L);
        long count = couponRepository.count();
        assertEquals(1, count);
    }

    @Test
    public void 여러명응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch =  new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Thread.sleep(10000);
        long count = couponRepository.count();
        assertEquals(100, count);
    }
}