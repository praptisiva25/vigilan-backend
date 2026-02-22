package com.vigilan.backend.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigilan.backend.dto.queue.MonitoringJobMessage;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.repository.MonitoringJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonitoringWorker {

    private final SqsClient sqsClient;
    private final MonitoringJobRepository jobRepository;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Scheduled(fixedDelay = 5000)
    public void pollQueue() {

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(5)
                .build();

        List<Message> messages = sqsClient.receiveMessage(request).messages();

        for (Message message : messages) {

            try {
                MonitoringJobMessage jobMessage =
                        objectMapper.readValue(message.body(), MonitoringJobMessage.class);

                processJob(jobMessage);

                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processJob(MonitoringJobMessage jobMessage) throws InterruptedException {

        MonitoringJob job = jobRepository.findById(jobMessage.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus("RUNNING");
        jobRepository.save(job);

        if ("LIVE".equalsIgnoreCase(job.getMode())) {

            // ~20 seconds simulation
            for (int i = 1; i <= 20; i++) {
                Thread.sleep(1000);
                job.setProgress(i * 5);
                jobRepository.save(job);
            }

        } else if ("FAST".equalsIgnoreCase(job.getMode())) {

            // ~2 seconds simulation
            for (int i = 1; i <= 10; i++) {
                Thread.sleep(200);
                job.setProgress(i * 10);
                jobRepository.save(job);
            }
        }

        job.setStatus("COMPLETED");
        job.setFinishedAt(LocalDateTime.now());
        jobRepository.save(job);
    }
}