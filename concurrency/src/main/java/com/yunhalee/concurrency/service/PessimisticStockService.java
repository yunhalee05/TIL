package com.yunhalee.concurrency.service;

import com.yunhalee.concurrency.domain.StockEntity;
import com.yunhalee.concurrency.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticStockService {

    private final StockRepository stockRepository;

    public PessimisticStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        StockEntity stock = stockRepository.findOneByIdWithPessimisticLock(id);
        stock.decrease(quantity);
        stockRepository.save(stock);
    }

}
