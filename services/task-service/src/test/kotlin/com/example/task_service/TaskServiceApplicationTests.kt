package com.example.taskservice.service

import com.example.task_service.model.Task
import com.example.task_service.repository.TaskRepository
import com.example.task_service.service.TaskService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TaskServiceTest {

	private val taskRepository = mockk<TaskRepository>()
	private val userClient = mockk<UserClient>()

	private val taskService = TaskService(taskRepository, userClient)

	@Test
	fun `should create task only if user exists`() {
		// given
		every { userClient.userExists("u1") } returns true
		every { taskRepository.save(any<Task>()) } answers { firstArg() }

		// when
		val task = taskService.createTask("Learn Day 4", "u1")

		// then
		assertEquals("u1", task.userId)
		assertEquals("Learn Day 4", task.title)
	}

	@Test
	fun `should throw exception if user does not exist`() {
		every { userClient.userExists("u999") } returns false

		assertThrows<IllegalArgumentException> {
			taskService.createTask("Invalid task", "u999")
		}
	}

}
