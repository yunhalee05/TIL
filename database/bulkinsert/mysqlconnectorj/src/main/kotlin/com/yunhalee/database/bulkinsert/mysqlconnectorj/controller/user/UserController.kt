package com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user.request.CreateUsersRequest
import com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user.request.DeleteUserRequest
import com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user.response.UserResponse
import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity
import com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userRepository: UserRepository,
) {

    @PostMapping("/users")
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

    @DeleteMapping("/users/many")
    fun deleteUsers(@RequestBody ids: DeleteUserRequest): ResponseEntity<Void> {
        userRepository.deleteAllById(ids.ids)
        return ResponseEntity.noContent().build()
    }
}