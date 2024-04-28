package com.yunhalee.spring_db.repository;

import com.yunhalee.spring_db.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.yunhalee.spring_db.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;
    private final Logger log = LoggerFactory.getLogger(MemberRepositoryV1Test.class);

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager - 항상 새로운 커넥션 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀링 : HikariProxyConnection -> JdbcConnection
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        log.info("start");
        // save
        Member member = new Member("memberV7", 10000);
        repositoryV1.save(member);

        // findById
        Member memberById = repositoryV1.findById(member.getMemberId());
        assertThat(memberById).isNotNull();

        // update: money: 10000 -> 20000
        repositoryV1.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryV1.findById(member.getMemberId());
        assertEquals(updatedMember.getMoney(), 20000);

        // delete
        repositoryV1.delete(member.getMemberId());
        assertThatThrownBy(() -> repositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }

}