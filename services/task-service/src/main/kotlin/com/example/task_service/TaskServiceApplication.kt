package com.example.task_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication
class TaskServiceApplication

fun main(args: Array<String>) {
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"))
    try {
        runApplication<TaskServiceApplication>(*args)
    } catch (t: Throwable) {
        t.printStackTrace()
        // ensure process exits so Gradle shows failure cause
        System.exit(1)
    }
}
