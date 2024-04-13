package com.yunhalee.concurrency.service;

import com.yunhalee.concurrency.domain.StockEntity;
import com.yunhalee.concurrency.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PessimisticStockServiceTest {

    @Autowired
    private PessimisticStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void setUp() {
        stockRepository.saveAndFlush(new StockEntity(1L, 100L));
    }

    @AfterEach
    public void clear() {
        stockRepository.deleteAll();
    }


    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        Long stockId = 1L;
        int threadCount = 100;
        // 비동기 적으로 요청을 실행할 수 있는 thread service
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        // 모든 요청이 완료될 떄까지 기다리게 할 수 있는 기능
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i=0;  i< threadCount; i++) {
            service.submit(() -> {
                try {
                    stockService.decrease(stockId, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        StockEntity stock = stockRepository.findById(stockId).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }

}