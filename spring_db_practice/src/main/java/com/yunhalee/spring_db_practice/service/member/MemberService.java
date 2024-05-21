package com.yunhalee.spring_db_practice.service.member;


import com.yunhalee.spring_db_practice.domain.Log;
import com.yunhalee.spring_db_practice.domain.Member;
import com.yunhalee.spring_db_practice.repository.member.LogRepository;
import com.yunhalee.spring_db_practice.repository.member.MemberRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    private final Logger log = org.slf4j.LoggerFactory.getLogger(MemberService.class);

    public MemberService(MemberRepository memberRepository, LogRepository logRepository) {
        this.memberRepository = memberRepository;
        this.logRepository = logRepository;
    }

    public void joinV1(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("== memberRepository 호출 시작 ==");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료 ==");

        log.info("== logRepository 호출 시작 ==");
        logRepository.save(logMessage);
        log.info("== logRepository 호출 종료 ==");
    }

    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("== memberRepository 호출 시작 ==");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료 ==");

        log.info("== logRepository 호출 시작 ==");
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage.getMessage());
            log.info("정상 흐름 반환");
        }
        log.info("== logRepository 호출 종료 ==");
    }
}
