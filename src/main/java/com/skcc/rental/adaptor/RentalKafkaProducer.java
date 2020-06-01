package com.skcc.rental.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.rental.config.KafkaProperties;
import com.skcc.rental.domain.UpdateBookEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class RentalKafkaProducer {

    private final Logger log = LoggerFactory.getLogger(RentalKafkaProducer.class);

    private static final String TOPIC = "topic_kafka";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(RentalKafkaProducer.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public RentalKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize(){
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    public void updateBookStatus(Long bookId, String bookStatus){
        try {

                UpdateBookEvent updateBookEvent = new UpdateBookEvent(bookId, bookStatus);
                String message = objectMapper.writeValueAsString(updateBookEvent);
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);
                producer.send(record);

        } catch (JsonProcessingException e) {
            logger.error("Could not send book List",e);
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void shutdown(){
        log.info("Shutdown Kafka producer");
        producer.close();
    }
}
