package com.yunhalee.concurrency_coroutine.repository

import com.yunhalee.concurrency_coroutine.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findOneByName(name: String) : Member?
}