package com.yunhalee.spring_db.exception.basic;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UncheckedTest {

    static Logger log = LoggerFactory.getLogger(UncheckedTest.class);


    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }


    /**
     * RuntimeException을 상속받은 예외는 체크 예외가 된다.
     */

    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Service {

        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 됨.
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                // 예외 처리 로직
                log.info("예외 처리, message = {}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 된다. 자연스럽게 상위로 넘어간다.
         * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 됨.
         */
        public void callThrow() {
            repository.call();
        }


    }

    static class Repository {

        public void call() {
            throw new MyUncheckedException("ex");
        }

    }
}
