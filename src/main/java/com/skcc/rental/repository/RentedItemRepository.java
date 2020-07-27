package com.skcc.rental.repository;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.RentedItem;

import com.skcc.rental.web.rest.dto.RentedItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the RentedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentedItemRepository extends JpaRepository<RentedItem, Long> {
    RentedItem findByBookId(Long bookId);
    List<RentedItem> findByRental(Rental rental);
    Page<RentedItem> findByBookTitleContaining(String bookTitle, Pageable pageable);
}
