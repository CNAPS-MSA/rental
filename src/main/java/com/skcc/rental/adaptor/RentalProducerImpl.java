package com.skcc.rental.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.rental.config.KafkaProperties;
import com.skcc.rental.domain.event.CatalogChanged;
import com.skcc.rental.domain.event.PointChanged;
import com.skcc.rental.domain.event.StockChanged;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;

@Service
public class RentalProducerImpl implements RentalProducer {

    private final Logger log = LoggerFactory.getLogger(RentalProducerImpl.class);

    private static final String TOPIC_BOOK = "topic_book";
    private static final String TOPIC_CATALOG = "topic_catalog";
    private static final String TOPIC_POINT = "topic_point";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(RentalProducerImpl.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public RentalProducerImpl(KafkaProperties kafkaProperties) {
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
    public void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException {

        StockChanged stockChanged = new StockChanged(bookId, bookStatus);
        String message = objectMapper.writeValueAsString(stockChanged);
        producer.send(new ProducerRecord<>(TOPIC_BOOK, message)).get();

    }

    // 권당 포인트 적립
    public void savePoints(Long userId, int points) throws ExecutionException, InterruptedException, JsonProcessingException {
        PointChanged pointChanged = new PointChanged(userId, points);
        String message = objectMapper.writeValueAsString(pointChanged);
        producer.send(new ProducerRecord<>(TOPIC_POINT, message)).get();
    }

    //대출, 반납  시 book catalog의 책 상태 업데이트
    public void updateBookCatalogStatus(Long bookId, String eventType) throws ExecutionException, InterruptedException,JsonProcessingException {
        CatalogChanged catalogChanged = new CatalogChanged();
        catalogChanged.setBookId(bookId);
        catalogChanged.setEventType(eventType);
        String message = objectMapper.writeValueAsString(catalogChanged);
        producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
    }

    @PreDestroy
    public void shutdown(){
        log.info("Shutdown Kafka producer");
        producer.close();
    }



}
