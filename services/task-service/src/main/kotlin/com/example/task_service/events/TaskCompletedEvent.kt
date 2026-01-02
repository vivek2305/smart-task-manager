package com.example.task_service.events

data class TaskCompletedEvent(
    val taskId: Long,
    val userId: String,
    val completedAt: Long = System.currentTimeMillis()
)
