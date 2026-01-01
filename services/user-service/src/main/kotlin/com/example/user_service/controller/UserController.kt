

package com.example.user_service.controller

import com.example.user_service.model.User
import com.example.user_service.repository.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository
) {

    @PostMapping
    fun createUser(@RequestBody user: User): User {
        return userRepository.save(user)
    }


    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): User? {
        return userRepository.findById(id)
    }
}
