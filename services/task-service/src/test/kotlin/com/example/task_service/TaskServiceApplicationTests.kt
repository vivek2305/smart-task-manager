package com.example.task_service.service

import com.example.task_service.client.UserServiceClient
import com.example.task_service.dtos.UserResponse
import com.example.task_service.exception.UserNotFoundException
import com.example.task_service.kafka.TaskEventPublisher
import com.example.task_service.model.Task
import com.example.task_service.repository.TaskRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TaskServiceTest {

    private val taskRepository = mockk<TaskRepository>()
    private val userClient = mockk<UserServiceClient>()
    private val eventProducer = mockk<TaskEventPublisher>(relaxed = true)

    private val taskService = TaskService(
        taskRepository = taskRepository,
        eventProducer = eventProducer,
        userServiceClient = userClient
    )

    @Test
    fun `should create task only if user exists`() = runBlocking {
        // given
        val userResponse = UserResponse(
            id = "u1",
            name = "Vivek",
            email = "vivek@test.com"
        )

        coEvery { userClient.getUserById("u1") } returns userResponse
        every { taskRepository.save(any<Task>()) } answers { firstArg() }

        // when
        val task = taskService.createTask("Learn Day 4", "u1")

        // then
        assertEquals("u1", task.userId)
        assertEquals("Learn Day 4", task.title)
        assertFalse(task.completed)

        coVerify(exactly = 1) { userClient.getUserById("u1") }
        verify(exactly = 1) { taskRepository.save(any<Task>()) }
    }


    @Test
    fun `should throw exception if user does not exist`() = runBlocking {
        // given
        coEvery { userClient.getUserById("u999") } throws
                UserNotFoundException("User not found: u999")

        // when + then
        val exception = assertThrows<IllegalArgumentException> {
            runBlocking {
                taskService.createTask("Invalid task", "u999")
            }
        }

        assertEquals("User does not exist", exception.message)

        coVerify(exactly = 1) { userClient.getUserById("u999") }
        verify { taskRepository wasNot Called }
        verify { eventProducer wasNot Called }
    }


    @Test
    fun `should publish kafka event when task is completed`() {
        // given
        val task = Task(
            id = 1L,
            title = "Learn Kafka",
            userId = "u1",
            completed = false
        )

        every { taskRepository.findById(1L) } returns Optional.of(task)
        every { taskRepository.save(any<Task>()) } answers { firstArg() }

        // when
        val completedTask = taskService.completeTask(1L)

        // then
        assertTrue(completedTask.completed)

        verify(exactly = 1) {
            eventProducer.publish(any())
        }
    }
}
