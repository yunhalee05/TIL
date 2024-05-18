package com.yunhalee.spring_db_practice.exception;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService service;

    @Test
    void runtimeException() {
        assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(RollbackService.MyException.class);
    }


    @Test
    void rollbackForCheckedException() {
        assertThatThrownBy(() -> service.rollbackForCheckedException())
                .isInstanceOf(RollbackService.MyException.class);
    }


    @TestConfiguration
    static class RollbackConfig {

        @Bean
        public RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    static class RollbackService {

        private final Logger log = LoggerFactory.getLogger(RollbackTest.class);


        // 런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException();
        }


        // 체크 예외 발생 : 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        // 체크 예외 noRollbackFor 지정 : 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackForCheckedException() throws MyException {
            log.info("call rollbackForCheckedException");
            throw new MyException();
        }

        static class MyException extends Exception {
        }
    }
}
