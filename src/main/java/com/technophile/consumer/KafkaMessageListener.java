package com.technophile.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technophile.dto.Customer;
import com.technophile.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KafkaMessageListener {
    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    /**
     * These consumers under same group ,assigned to each partition to increase the throughput
     * but it is not recommended way instead of it use concurrency
     *
     */
//    @KafkaListener(topics = "sunday",groupId = "weekend")
//    public void consume1(String message){
//        log.info("Consumer1 consume the message {}",message);
//    }
//    @KafkaListener(topics = "java-demo1",groupId = "kafka-group-new")
//    public void consume2(String message){
//        log.info("Consumer2 consume the message {}",message);
//    }
//    @KafkaListener(topics = "java-demo1",groupId = "kafka-group-new")
//    public void consume3(String message){
//        log.info("Consumer3 consume the message {}",message);
//    }
//    @KafkaListener(topics = "java-demo1",groupId = "kafka-group-new")
//    public void consume4(String message){
//        log.info("Consumer4 consume the message {}",message);
//    }

/*    @KafkaListener(topics = "java-demo",groupId = "kafka-group-new")
    public void consume4(Customer customer){
        log.info("Consumer consume the customer events {}",customer);
    }

    *//**
     * Consume message from specific partition
     * @param message
     *//*
    @KafkaListener(topics = "sunday",groupId = "weekend",
            topicPartitions = {@TopicPartition(topic = "sunday",partitions = "0")})
    public void consumeMessageFromSpecificPartition(String message){
        log.info("ConsumeMessageFromSpecificPartition the message {}",message);
    }*/

    @RetryableTopic(attempts = "4")// 3 topic N-1
    @KafkaListener(topics = "${app.topic.name}", groupId = "kafka-dlt-group")
    public void consumeEvents(User user, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            log.info("Received: {} from {} offset {}", new ObjectMapper().writeValueAsString(user), topic, offset);
            //validate restricted IP before process the records
            List<String> restrictedIpList = Stream.of("32.241.244.236", "15.55.49.164", "81.1.95.253", "126.130.43.183").collect(Collectors.toList());
            if (restrictedIpList.contains(user.getIpAddress())) {
                throw new RuntimeException("Invalid IP Address received !");
            }

        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            log.error("Error ::",e);
        }
    }

    @DltHandler
    public void listenDLT(User user, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("DLT Received : {} , from {} , offset {}",user.getFirstName(),topic,offset);
    }
}
