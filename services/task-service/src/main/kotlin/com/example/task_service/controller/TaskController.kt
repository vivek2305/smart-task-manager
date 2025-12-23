package com.example.task_service.controller

import com.example.task_service.model.Task
import com.example.task_service.service.TaskService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    fun createTask(@RequestParam title: String): Task {
        return taskService.createTask(title)
    }

    @GetMapping
    fun getTasks(): List<Task> {
        return taskService.getAllTasks()
    }

    @PutMapping("/{id}/complete")
    fun completeTask(@PathVariable id: Long): Task {
        return taskService.completeTask(id)
    }
}
