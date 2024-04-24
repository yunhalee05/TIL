package com.yunhalee.concurrency_coroutine.service

import com.yunhalee.concurrency_coroutine.repository.MemberRepository
import kotlinx.coroutines.delay
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    @Transactional
    suspend fun delete(name: String) {
        memberRepository.findOneByName(name)?.let { member ->
            println("--------------------deletemember찾음 ---------- member: ${member.id}")
            delay(3000)
            memberRepository.delete(member)
            println("--------------------deletemember 삭제 -------------")
//            Thread.sleep(1500)
            println("--------------------deletemember thread sleep 끝  ---------- member: ${member.id}")
        }
    }

    @Transactional
    suspend fun updateName(id: Long, name: String) {
        memberRepository.findByIdOrNull(id)?.let { member ->
            println("--------------------updatemember찾음 ---------- member: ${member.id}")
            member.name = name
            println("--------------------updatemember 이름 바꿈 -------------")

//            Thread.sleep(1000)
            delay(3000)
            memberRepository.save(member)
            println("--------------------updatemember thread sleep 끝  ---------- member: ${member.id}")


        }
    }


    @Transactional
    fun delete2(name: String) {
        memberRepository.findOneByName(name)?.let { member ->
            println("--------------------deletemember찾음 ---------- member: ${member.id}")
            Thread.sleep(4000)
            memberRepository.delete(member)
            println("--------------------deletemember 삭제 -------------")
//            Thread.sleep(1500)
            println("--------------------deletemember thread sleep 끝  ---------- member: ${member.id}")
        }
    }

    @Transactional
    fun updateName2(id: Long, name: String) {
        Thread.sleep(500)

        memberRepository.findByIdOrNull(id)?.let { member ->
            println("--------------------updatemember찾음 ---------- member: ${member.id}")
            member.name = name
            println("--------------------updatemember 이름 바꿈 -------------")

//            Thread.sleep(1000)
            Thread.sleep(3000)
            memberRepository.save(member)
            println("--------------------updatemember thread sleep 끝  ---------- member: ${member.id}")


        }
    }



}