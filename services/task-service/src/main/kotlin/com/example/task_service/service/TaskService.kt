package com.example.task_service.service

import com.example.task_service.model.Task
import com.example.task_service.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository
) {

    fun createTask(title: String): Task {
        val task = Task(title = title)
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
