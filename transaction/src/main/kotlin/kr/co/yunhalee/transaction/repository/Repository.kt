package kr.co.yunhalee.transaction.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Repository : JpaRepository<Entity, Long>