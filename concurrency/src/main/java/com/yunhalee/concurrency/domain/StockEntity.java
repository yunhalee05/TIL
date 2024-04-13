package com.yunhalee.concurrency.domain;

import jakarta.persistence.*;

@Entity
public class StockEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long quantity;

    @Version
    private Long version;

    public StockEntity() {
    }

    public StockEntity(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity) {
        if (this.quantity - quantity <0) {
            throw new RuntimeException("재고를 0개 미만으로 설정할 수 없습니다.");
        }
        this.quantity -= quantity;
    }
}
