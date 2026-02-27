package com.vigilan.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigilan.backend.dto.queue.MonitoringJobMessage;
import com.vigilan.backend.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${AWS_SQS_QUEUE_URL}")
    private String queueUrl;

    @Override
    public void sendMonitoringJob(MonitoringJobMessage message) {

        try {
            String body = objectMapper.writeValueAsString(message);

            System.out.println("Sending job to SQS:");
            System.out.println("Queue URL: " + queueUrl);
            System.out.println("Message Body: " + body);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build();

            sqsClient.sendMessage(request);

            System.out.println("Message successfully sent to SQS.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send monitoring job to SQS", e);
        }
    }
}