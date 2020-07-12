package com.skcc.rental.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.rental.config.KafkaProperties;
import com.skcc.rental.domain.event.BookCatalogChanged;
import com.skcc.rental.domain.event.PointChanged;
import com.skcc.rental.domain.event.StockChanged;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Service
public class RentalProducer {

    private final Logger log = LoggerFactory.getLogger(RentalProducer.class);

    private static final String TOPIC_BOOK = "topic_book";
    private static final String TOPIC_CATALOG = "topic_catalog";
    private static final String TOPIC_POINT = "topic_point";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(RentalProducer.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public RentalProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize(){
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }


    /******
     * kafka 메세지 수신 후, 결과 메세지 받도록 변경
     *
     * *******/

    //책 상태 업데이트
    public PublishResult updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException {

        StockChanged stockChanged = new StockChanged(bookId, bookStatus);
        String message = objectMapper.writeValueAsString(stockChanged);
        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC_BOOK, message)).get();
        return new PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));

    }

    // 권당 포인트 적립
    public PublishResult savePoints(Long userId, int points) throws ExecutionException, InterruptedException, JsonProcessingException {
        PointChanged pointChanged = new PointChanged(userId, points);
        String message = objectMapper.writeValueAsString(pointChanged);
        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC_POINT, message)).get();
        return new PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));
    }

    //대여, 반납  시 book catalog의 책 상태 업데이트
    public PublishResult updateBookCatalogStatus(Long bookId, String eventType) throws ExecutionException, InterruptedException,JsonProcessingException {
        BookCatalogChanged bookCatalogChanged = new BookCatalogChanged();
        bookCatalogChanged.setBookId(bookId);
        bookCatalogChanged.setEventType(eventType);
        String message = objectMapper.writeValueAsString(bookCatalogChanged);
        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
        return new PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));
    }

    @PreDestroy
    public void shutdown(){
        log.info("Shutdown Kafka producer");
        producer.close();
    }


    private static class PublishResult {
        public final String topic;
        public final int partition;
        public final long offset;
        public final Instant timestamp;

        private PublishResult(String topic, int partition, long offset, Instant timestamp) {
            this.topic = topic;
            this.partition = partition;
            this.offset = offset;
            this.timestamp = timestamp;
        }
    }
}
