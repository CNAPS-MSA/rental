package com.skcc.rental.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface RentalProducer {


    //kafka - 책 상태 변경 - book service
    void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException;
    //kafka - 포인트 적립  - user service(gateway)
    void savePoints(Long userId, int pointPerBooks) throws ExecutionException, InterruptedException, JsonProcessingException;
    //kafka - 책 상태 변경  - bookCatalog service
    void updateBookCatalogStatus(Long bookId, String eventType) throws InterruptedException, ExecutionException, JsonProcessingException;

}
