package com.yunhalee.transaction.transaction.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Repository : JpaRepository<TestEntity, Long>