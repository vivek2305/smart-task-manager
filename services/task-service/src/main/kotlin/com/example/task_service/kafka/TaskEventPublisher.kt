package com.example.task_service.kafka

//package com.example.task_service.sqs

import com.example.task_service.events.TaskCompletedEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import tools.jackson.databind.ObjectMapper

@Component
class TaskEventPublisher(
    private val sqsClient: SqsClient,
    private val objectMapper: ObjectMapper,
    @Value("\${aws.sqs.queue-name}") private val queueName: String
) {

    fun publish(event: TaskCompletedEvent) {
        val queueUrl = sqsClient.getQueueUrl {
            it.queueName(queueName)
        }.queueUrl()

        val messageBody = objectMapper.writeValueAsString(event)

        sqsClient.sendMessage(
            SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build()
        )

        println("✅ SQS message published: $messageBody")
    }
}

