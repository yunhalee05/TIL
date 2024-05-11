package com.yunhalee.concurrency.coroutine.service

import com.yunhalee.concurrency.coroutine.domain.Member
import com.yunhalee.concurrency.coroutine.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ConcurrentMemberService(
    val memberRepository: MemberRepository
) {

    fun saveMember(name: String) {
        memberRepository.save(Member(name = name))
    }
}
