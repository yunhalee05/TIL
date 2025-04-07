package org.yunhalee.database.bulkinsert.mariadbconnectorj.user

import org.yunhalee.database.bulkinsert.mariadbconnectorj.user.request.CreateUsersRequest
import org.yunhalee.database.bulkinsert.mariadbconnectorj.user.request.DeleteUserRequest
import org.yunhalee.database.bulkinsert.mariadbconnectorj.user.response.UserResponse
import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user.UserEntity
import org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.user.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository,
) {

    @PostMapping("/save-all")
    fun createAll(@RequestBody requests: CreateUsersRequest): ResponseEntity<List<UserResponse>> {
        val responses = userRepository.saveAll(requests.requests.map { request ->
            UserEntity(
                email = request.email,
                name = request.name,
                phone = request.phone,
            )
        })
        return ResponseEntity.ok(responses.map { user ->
            UserResponse(
                id = user.id,
                email = user.email,
                name = user.name,
                phone = user.phone,
            )
        })
    }


    @PostMapping("/save-bulk-insert")
    fun createAllByBulkInsert(@RequestBody requests: CreateUsersRequest): ResponseEntity<List<UserResponse>> {
        val responses = userRepository.saveAllWithBulkInsert(requests.requests.map { request ->
            UserEntity(
                email = request.email,
                name = request.name,
                phone = request.phone,
            )
        })
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/users/many")
    fun deleteUsers(@RequestBody ids: DeleteUserRequest): ResponseEntity<Void> {
        userRepository.deleteAllById(ids.ids)
        return ResponseEntity.noContent().build()
    }
}