package com.yunhalee.spring_db.connection;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.yunhalee.spring_db.connection.ConnectionConst.*;

public class DBConnectionUtil {


    public static Connection getConnection() {
        Logger log = LoggerFactory.getLogger(DBConnectionUtil.class);

        try {
            //  get connection=conn0: url=jdbc:h2:tcp://localhost/~/Developer/test user=SA, class=class org.h2.jdbc.JdbcConnection
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}



