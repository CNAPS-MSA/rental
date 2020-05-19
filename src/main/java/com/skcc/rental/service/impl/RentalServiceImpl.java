package com.skcc.rental.service.impl;

import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.domain.ReturnedItem;
import com.skcc.rental.domain.enumeration.RentalStatus;
import com.skcc.rental.repository.RentedItemRepository;
import com.skcc.rental.repository.ReturnedItemRepository;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.repository.RentalRepository;
import com.skcc.rental.service.dto.RentalDTO;
import com.skcc.rental.service.mapper.RentalMapper;
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
public class RentalServiceImpl implements RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentedItemRepository rentedItemRepository;

    private final ReturnedItemRepository returnedItemRepository;

    private final RentalMapper rentalMapper;

    public RentalServiceImpl(RentalRepository rentalRepository, RentedItemRepository rentedItemRepository, ReturnedItemRepository returnedItemRepository, RentalMapper rentalMapper) {
        this.rentalRepository = rentalRepository;
        this.rentedItemRepository = rentedItemRepository;
        this.returnedItemRepository = returnedItemRepository;
        this.rentalMapper = rentalMapper;
    }

    /**
     * Save a rental.
     *
     * @param rentalDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RentalDTO save(RentalDTO rentalDTO) {
        log.debug("Request to save Rental : {}", rentalDTO);
        Rental rental = rentalMapper.toEntity(rentalDTO);
        rental = rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RentalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll(pageable)
            .map(rentalMapper::toDto);
    }

    /**
     * Get one rental by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RentalDTO> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id)
            .map(rentalMapper::toDto);
    }

    /**
     * Delete the rental by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }

    @Override
    public RentalDTO rentBooks(Long userId, List<Long> bookIds) {
        log.debug("Rent Books by : ", userId, " Book List : ", bookIds);

        if(rentalRepository.findByUserId(userId).isPresent()){ //기존에 대여 내역이 있는 경우
            Rental rental = rentalRepository.findByUserId(userId).get();
            rental = rental.rentBooks(bookIds);
            if(rental!=null)
            {
                log.debug(" 대여 완료 되었습니다.", rental);
                return rentalMapper.toDto(rental);
            }else{
                log.debug("대여 불가능 상태입니다.");
                return null;
            }
        }else{ // 첫 대여인 경우
            log.debug("첫 도서 대여입니다.");
            Rental rental = Rental.createRental(userId);
            rentalRepository.save(rental);
            rental=rental.rentBooks(bookIds);

            log.debug(" 대여 완료 되었습니다.", rental);
            return rentalMapper.toDto(rental);
        }
    }

    @Override
    public void returnBooks(Long userId, List<Long> bookIds) {
        log.debug("Return books by ", userId, " Return Book List : ", bookIds);

        if(rentalRepository.findByUserId(userId).isPresent()){
            Rental rental = rentalRepository.findByUserId(userId).get();
            for(Long bookId: bookIds){
                RentedItem rentedItem = rentedItemRepository.findByBookId(bookId).get();
                rental.getRentedItems().remove(rentedItem);
                rentedItemRepository.delete(rentedItem);
                ReturnedItem returnedItem = ReturnedItem.createReturnedItem(rental, bookId , LocalDate.now());
                rental.addReturnedItem(returnedItem);
                returnedItemRepository.save(returnedItem);

            }

            if(rental.getRentedItems().size()==0 && rental.getRentalStatus()!= RentalStatus.OVERDUE){
                rental.setRentalStatus(RentalStatus.OK);
            }

            rentalRepository.save(rental);

            return ;

        }else{
            log.debug("대여 이력이 없습니다.");
            return ;
        }

    }
}
