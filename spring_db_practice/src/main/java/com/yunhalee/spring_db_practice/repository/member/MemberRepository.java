package com.yunhalee.spring_db_practice.repository.member;

import com.yunhalee.spring_db_practice.domain.Member;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final EntityManager em;
    private final Logger log = LoggerFactory.getLogger(MemberRepository.class);

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void save(Member member) {
        log.info("member 저장");
        em.persist(member);
    }

    public Optional<Member> find(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findAny();
    }
}
