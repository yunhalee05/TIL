package com.yunhalee.spring_db.repository;

import com.yunhalee.spring_db.domain.Member;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryVOTest {

    MemberRepositoryVO repositoryVO = new MemberRepositoryVO();
    private final Logger log = LoggerFactory.getLogger(MemberRepositoryVOTest.class);

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV6", 10000);
        repositoryVO.save(member);

        // findById
        Member findMember = repositoryVO.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);
        assertFalse(findMember == member);

        // update: moeny: 10000 -> 20000
        repositoryVO.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryVO.findById(member.getMemberId());
        assertEquals(updatedMember.getMoney(), 20000);

        // delete
        repositoryVO.delete(member.getMemberId());
        assertThatThrownBy(() -> repositoryVO.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }

}