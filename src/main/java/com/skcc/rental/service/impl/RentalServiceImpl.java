package com.skcc.rental.service.impl;

import com.skcc.rental.domain.RentedItem;

import com.skcc.rental.repository.RentedItemRepository;
import com.skcc.rental.repository.ReturnedItemRepository;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.repository.RentalRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Rental}.
 */
@Service
@Transactional
public class RentalServiceImpl {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentedItemRepository rentedItemRepository;

    private final ReturnedItemRepository returnedItemRepository;

    //private final RentalMapper rentalMapper;

    public RentalServiceImpl(RentalRepository rentalRepository, RentedItemRepository rentedItemRepository, ReturnedItemRepository returnedItemRepository) {
        this.rentalRepository = rentalRepository;
        this.rentedItemRepository = rentedItemRepository;
        this.returnedItemRepository = returnedItemRepository;
        //this.rentalMapper = rentalMapper;
    }


    /**
     * Save a rental.
     *
     * @param
     * @return the persisted entity.
     */

    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */


    @Transactional(readOnly = true)
    public Page<Rental> findAll(Pageable pageable) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll(pageable);
    }

    /**
     * Get one rental by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    @Transactional(readOnly = true)
    public Optional<Rental> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id);

    }

    /**
     * Delete the rental by id.
     *
     * @param id the id of the entity.
     */

    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }


    @Transactional
    public Rental rentBooks(Long userId, List<Long> bookIds) {
        log.debug("Rent Books by : ", userId, " Book List : ", bookIds);
        System.out.println("Rent Books by : " + userId + " Book List : "+ bookIds);

        Rental rental = new Rental();

        if(rentalRepository.findByUserId(userId).isPresent()) { //기존에 대여 내역이 있는 경우
            //도서 카드 가져와서
            System.out.println("second");
            rental = rentalRepository.findByUserId(userId).get();
        }else
        {
            //도서카드 새로 생성
            log.debug("첫 도서 대여입니다.");
            System.out.println("first");
            rental = Rental.createRental(userId);
        }

        for (Long bookId:bookIds) {
            //대여도서 객체 만들고
            rental = rentBook(rental, bookId);
        }
        //도서카드 저장
        Rental saveRental = rentalRepository.save(rental);

        if(saveRental==null)
        {
            log.debug("대여 불가능 상태입니다.");
            return null;
        }

        log.debug(" 대여 완료 되었습니다.", rental);
        return saveRental;
    }


    @Transactional
    public Rental returnBooks(Long userId,Long bookId) {
        log.debug("Return books by ", userId, " Return Book List : ", bookId);

        Rental rental = new Rental();
        rental = rentalRepository.findByUserId(userId).get();

        System.out.println(rental.toString());
        if(rental == null) {
            log.debug("대여 이력이 없습니다.");
            return null;
        }

        RentedItem rentedItem1 = rental.getRentedItems().stream().filter(rentedItem -> rentedItem.getBookId().equals(bookId)).findFirst().get();
        //System.out.println(rentedItem1.toString());
        rental.returnBooks(rentedItem1);
        return rentalRepository.save(rental);
    }

    public Rental rentBook(Rental rental, Long bookId) {
        //원래는 book서비스 호출해서 book객체정보 가져와서 대여도서 생성햐야 함. 이 부분 리소스로 뺴기로 함.
        RentedItem rentedItem = RentedItem.createRentedItem(bookId,"어린왕자", LocalDate.now());
        //도서카드에다가 대여도서 추가
        rental = rental.rentBooks(rentedItem);
        return rental;
    }


}
