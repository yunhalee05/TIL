package com.yunhalee.database.bulkinsert.mysqlconnectorj

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity
import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserStatus
import com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun save() {
        val users = mutableListOf<UserEntity>()
        for (i in 1..1000) {
            users.add(UserEntity(email = "testUser$i@gmail.com", name = "testUser$i", phone = "010-1234-567$i", status = UserStatus.ACTIVE))
        }
        val start = System.currentTimeMillis()
        for (user in users) {
            // SimpleJpaRepository -> SessionImpl -> DefaultMergeEventListener
            userRepository.save(user)
        }
        val end = System.currentTimeMillis()
        // Time taken to save 1000 users: 11681 ms
        println("Time taken to save 1000 users: ${end - start} ms")
    }

    @Test
    @Transactional
    fun saveWithTransactional() {
        val users = mutableListOf<UserEntity>()
        for (i in 1..1000) {
            users.add(UserEntity(email = "testUser$i@gmail.com", name = "testUser$i", phone = "010-1234-567$i", status = UserStatus.ACTIVE))
        }
        val start = System.currentTimeMillis()
        for (user in users) {
            userRepository.save(user)
        }
        val end = System.currentTimeMillis()
        // Time taken to save 1000 users: 4094 ms
        println("Time taken to save 1000 users: ${end - start} ms")
    }


    @Test
    fun saveAll() {
        val users = mutableListOf<UserEntity>()
        for (i in 1..1000) {
            users.add(UserEntity(email = "testUser$i@gmail.com", name = "testUser$i", phone = "010-1234-567$i", status = UserStatus.ACTIVE))
        }
        val start = System.currentTimeMillis()
        userRepository.saveAll(users)
        val end = System.currentTimeMillis()
        // Time taken to save 1000 users: 3821 ms
        println("Time taken to save 1000 users: ${end - start} ms")
    }

    @Test
    fun bulk_insert() {
        val users = mutableListOf<UserEntity>()
        for (i in 1..1000) {
            users.add(UserEntity(email = "testUser$i@gmail.com", name = "testUser$i", phone = "010-1234-567$i", status = UserStatus.ACTIVE))
        }
        val start = System.currentTimeMillis()
        userRepository.saveAllWithBulkInsert(users)
        val end = System.currentTimeMillis()
        // Time taken to save 1000 users: 2419 ms
        println("Time taken to save 1000 users: ${end - start} ms")
    }


}