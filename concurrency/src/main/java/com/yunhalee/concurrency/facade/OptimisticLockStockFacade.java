package com.yunhalee.concurrency.facade;

import com.yunhalee.concurrency.service.OptimisticStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {


    private final OptimisticStockService optimisticStockService;

    public OptimisticLockStockFacade(OptimisticStockService optimisticStockService) {
        this.optimisticStockService = optimisticStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {

        while (true) {
            try {
                optimisticStockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
