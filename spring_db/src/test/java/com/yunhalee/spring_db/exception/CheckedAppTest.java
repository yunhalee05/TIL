package com.yunhalee.spring_db.exception;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.net.ConnectException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CheckedAppTest {

    static Logger log = LoggerFactory.getLogger(CheckedAppTest.class);


    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }


    }

    static class NetworkClient {

        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }

    }

    static class Repository {

        public void call() throws SQLException {
            throw new SQLException("ex");
        }

    }
}
