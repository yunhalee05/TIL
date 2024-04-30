package com.yunhalee.spring_db.service;

import com.yunhalee.spring_db.domain.Member;
import com.yunhalee.spring_db.repository.MemberRepositoryV2;
import com.yunhalee.spring_db.repository.MemberRepositoryV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 = 트랜잭션 매니저
 */
public class MemberServiceV3_1 {

    private final Logger log = LoggerFactory.getLogger(MemberServiceV3_1.class);

    private final PlatformTransactionManager transactionManager;

    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_1(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.transactionManager = transactionManager;
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(fromId, toId, money);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

}
