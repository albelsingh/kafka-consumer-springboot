package com.technophile.consumer;

import com.technophile.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {
    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

//    /**
//     * These consumers under same group ,assigned to each partition to increase the throughput
//     * but it is not recommended way instead of it use concurrency
//     * @param message
//     */
//    @KafkaListener(topics = "java-demo1",groupId = "kafka-group-new")
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

    @KafkaListener(topics = "java-demo",groupId = "kafka-group-new")
    public void consume4(Customer customer){
        log.info("Consumer consume the customer events {}",customer);
    }
}
