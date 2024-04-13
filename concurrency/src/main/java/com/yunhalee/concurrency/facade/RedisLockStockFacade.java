package com.yunhalee.concurrency.facade;

import com.yunhalee.concurrency.repository.RedisLockRepository;
import com.yunhalee.concurrency.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class RedisLockStockFacade {


    private RedisLockRepository redisLockRepository;

    private final StockService stockService;

    public RedisLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
