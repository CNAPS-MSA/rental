package com.skcc.rental.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.domain.event.UserIdCreated;
import com.skcc.rental.web.rest.dto.LatefeeDTO;
import com.skcc.rental.web.rest.errors.RentUnavailableException;
import com.skcc.rental.web.rest.dto.BookInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service Interface for managing {@link com.skcc.rental.domain.Rental}.
 */
public interface RentalService {

    /**
     * Save a rental.
     *
     * @param rentalDTO the entity to save.
     * @return the persisted entity.
     */
    Rental save(Rental rentalDTO);

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rental> findAll(Pageable pageable);

    /**
     * Get the "id" rental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rental> findOne(Long id);

    /**
     * Delete the "id" rental.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    /**
     *
     * User 생성시 Rental 생성
     *
     *
     * **/
    Rental createRental(UserIdCreated userIdCreated);

    /****
     *
     * Business Logic
     *
     * 책 대여하기
     *
     * ****/
    RentedItem rentBook(Long userId, BookInfoDTO book) throws InterruptedException, ExecutionException, JsonProcessingException, RentUnavailableException;

    /****
     *
     * Business Logic
     *
     * 책 반납하기
     *
     * ****/
    Rental returnBook(Long userId, Long bookIds) throws ExecutionException, InterruptedException, JsonProcessingException;
    /****
     *
     * Business Logic
     *
     * 연체 도서 반납하기
     *
     * ****/
    Rental returnOverdueBooks(Long userid, Long book) throws ExecutionException, InterruptedException, JsonProcessingException;
    /****
     *
     * Business Logic
     *
     * 연체 상태 해제하기
     *
     * ****/
    Rental releaseOverdue(Long userId);
    /****
     *
     * Business Logic
     *
     * 연체 상태로 변경
     *
     * ****/
    Long beOverdueBooks(Long rentalId, Long bookId);

    //kafka - 책 상태 변경 - book service
    void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException;
    //kafka - 포인트 적립  - user service(gateway)
    void savePoints(Long userId) throws ExecutionException, InterruptedException, JsonProcessingException;
    //kafka - 책 상태 변경  - bookCatalog service
    void updateBookCatalog(Long bookId, String eventType) throws InterruptedException, ExecutionException, JsonProcessingException;
    //Rental 조회
    Optional<Rental> findRentalByUser(Long userId);
    //연체료 조회
    int findLatefee(Long userId);


}
