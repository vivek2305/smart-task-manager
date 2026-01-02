package com.example.task_service.service

import com.example.task_service.client.UserServiceClient
import com.example.task_service.events.TaskCompletedEvent
import com.example.task_service.exception.UserNotFoundException
import com.example.task_service.kafka.TaskEventPublisher
import com.example.task_service.model.Task
import com.example.task_service.repository.TaskRepository
import org.springframework.stereotype.Service


@Service
class TaskService(
    private val userServiceClient: UserServiceClient,
    private val taskRepository: TaskRepository,
    private val eventProducer: TaskEventPublisher
) {

    suspend fun createTask(userId: String, title: String): Task {
        //Validate user exists (sync business call)
        try {
            userServiceClient.getUserById(userId) // suspend call
        } catch (ex: UserNotFoundException) {
            throw IllegalArgumentException("User does not exist")
        }

        //Save task to PostgreSQL
        val task = Task(
            userId = userId,
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
        taskRepository.save(completedTask)

        //  Publish Event
        eventProducer.publish(
            TaskCompletedEvent(
                taskId = completedTask.id!!,
                userId = completedTask.userId
            )
        )

        return completedTask
    }
}

