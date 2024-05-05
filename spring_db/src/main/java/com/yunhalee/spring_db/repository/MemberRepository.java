package com.yunhalee.spring_db.repository;

import com.yunhalee.spring_db.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    Member findById(String memberId);

    void update(String memberId, int money);

    void delete(String memberId);
}
