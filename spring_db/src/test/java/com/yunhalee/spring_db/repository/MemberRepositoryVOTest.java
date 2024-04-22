package com.yunhalee.spring_db.repository;

import com.yunhalee.spring_db.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryVOTest {

    MemberRepositoryVO repositoryVO = new MemberRepositoryVO();

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV0", 10000);
        repositoryVO.save(member);
    }

}