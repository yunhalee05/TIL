package org.yunhalee.database.bulkinsert.mariadbconnectorj


import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user.UserEntity
import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user.UserStatus
import org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.user.UserRepository

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


    @Test
    fun bulk_insertWithRewriteBatchedStatementsOption() {
        val users = mutableListOf<UserEntity>()
        for (i in 1..1000) {
            users.add(UserEntity(email = "testUser$i@gmail.com", name = "testUser$i", phone = "010-1234-567$i", status = UserStatus.ACTIVE))
        }
        val start = System.currentTimeMillis()
        userRepository.saveAllWithBulkInsert(users)
        val end = System.currentTimeMillis()
        // Time taken to save 1000 users: 201 ms
        println("Time taken to save 1000 users: ${end - start} ms")
    }


}