package com.yunhalee.concurrency.transaction;


import com.yunhalee.concurrency.service.StockService;

public class TransactionStockService {

    private StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();
        stockService.decrease(id, quantity);
        endTransaction();
    }

    private void startTransaction() {
        System.out.println("start transaction");
    }


    private void endTransaction() {
        System.out.println("end transaction");
    }



}
