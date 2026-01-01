package com.example.task_service.client

import com.example.task_service.dtos.UserResponse
import com.example.task_service.exception.UserNotFoundException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.error
import kotlin.jvm.java

@Component
class UserServiceClient(
    private val webClient: WebClient
) {

    suspend fun getUserById(userId: String): UserResponse =
        webClient.get()
            .uri("/users/{id}", userId)
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                error(UserNotFoundException("User not found: $userId"))
            }
            .onStatus({ it.is5xxServerError }) {
                error(RuntimeException("User service error"))
            }
            .bodyToMono(UserResponse::class.java)
            .awaitSingle()
}
