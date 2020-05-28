package com.skcc.rental.repository;

import com.skcc.rental.domain.RentedItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RentedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentedItemRepository extends JpaRepository<RentedItem, Long> {
    RentedItem findByBookId(Long bookId);
}
