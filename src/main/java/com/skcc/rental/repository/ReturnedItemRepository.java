package com.skcc.rental.repository;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.ReturnedItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReturnedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnedItemRepository extends JpaRepository<ReturnedItem, Long> {
    ReturnedItem findByBookId(Long bookId);

    Page<ReturnedItem> findByRental(Rental rental, Pageable pageable);
}
