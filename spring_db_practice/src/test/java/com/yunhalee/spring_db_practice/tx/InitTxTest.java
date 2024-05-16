package com.yunhalee.spring_db_practice.tx;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
class InitTxTest {

    private final Logger log = LoggerFactory.getLogger(InitTxTest.class);

    @Autowired
    Hello hello;

    @Test
    void go() {
        // 초기화 코드는 스프링이 초기화 시점에 호출한다.

    }

    @TestConfiguration
    static class InternalCallV1Config {

        @Bean
        public Hello hello() {
            return new Hello();
        }

    }

    static class Hello {

        private final Logger log = LoggerFactory.getLogger(Hello.class);

        @PostConstruct
        @Transactional
        public void initV1() {
            log.info("Hello init @PostConstruct tx active={}", TransactionSynchronizationManager.isActualTransactionActive());
        }

        @EventListener(value = ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            log.info("Hello init ApplicationReadEvent tx active={}", TransactionSynchronizationManager.isActualTransactionActive());
        }
    }

    static class InternalService {

        private final Logger log = LoggerFactory.getLogger(InternalService.class);

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }
}
