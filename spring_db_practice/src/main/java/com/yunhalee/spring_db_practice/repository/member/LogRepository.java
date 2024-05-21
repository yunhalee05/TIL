package com.yunhalee.spring_db_practice.repository.member;

import com.yunhalee.spring_db_practice.domain.Log;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class LogRepository {

    private final EntityManager em;
    private final Logger log = org.slf4j.LoggerFactory.getLogger(LogRepository.class);

    public LogRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void save(Log logMessage) {
        log.info("log 저장");
        em.persist(logMessage);

        if (logMessage.getMessage().contains("로그 예외")) {
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public Optional<Log> find(String message) {
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList()
                .stream()
                .findAny();
    }
}
