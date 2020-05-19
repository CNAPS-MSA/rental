package com.skcc.rental.repository;

import com.skcc.rental.domain.RentedItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the RentedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentedItemRepository extends JpaRepository<RentedItem, Long> {

    Optional<RentedItem> findByBookId(Long bookId);
}
