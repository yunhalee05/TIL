package com.yunhalee.spring_db.repository;

import com.yunhalee.spring_db.domain.Member;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryVOTest {

    MemberRepositoryVO repositoryVO = new MemberRepositoryVO();
    private final Logger log = LoggerFactory.getLogger(MemberRepositoryVOTest.class);

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV3", 10000);
        repositoryVO.save(member);

        // findById
        Member findMember = repositoryVO.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);
        assertFalse(findMember == member);
    }

}