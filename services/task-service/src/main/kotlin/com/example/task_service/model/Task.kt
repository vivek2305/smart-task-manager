package com.example.task_service.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    val completed: Boolean = false,

    val createdAt: LocalDateTime = LocalDateTime.now()
)
