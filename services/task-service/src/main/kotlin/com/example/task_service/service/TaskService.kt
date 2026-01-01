package com.example.task_service.service

import com.example.task_service.client.UserServiceClient
import com.example.task_service.model.Task
import com.example.task_service.repository.TaskRepository
import org.springframework.stereotype.Service


@Service
class TaskService(
    private val userServiceClient: UserServiceClient,
    private val taskRepository: TaskRepository
) {

    suspend fun createTask(userId: String, title: String): Task {
        //Validate user exists (sync business call)
        val user = userServiceClient.getUserById(userId)

        //Save task to PostgreSQL
        val task = Task(
            userId = user.id,
            title = title
        )

        return taskRepository.save(task)
    }

    fun getAllTasks(): List<Task> =
        taskRepository.findAll()

    fun completeTask(id: Long): Task {
        val task = taskRepository.findById(id)
            .orElseThrow { RuntimeException("Task not found") }

        val completedTask = task.copy(completed = true)
        return taskRepository.save(completedTask)
    }
}

