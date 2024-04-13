package com.yunhalee.concurrency.repository;

import com.yunhalee.concurrency.domain.StockEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

    // spring data jpa에서는 어노테이션으로 락을 구현할 수 있다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    // named query를 작성해준다.
    @Query("select s from StockEntity s where s.id = :id")
    StockEntity findOneByIdWithPessimisticLock(Long id);


    @Lock(LockModeType.OPTIMISTIC)
    // named query를 작성해준다.
    @Query("select s from StockEntity s where s.id = :id")
    StockEntity findOneByIdWithOptimisticLock(Long id);

}
