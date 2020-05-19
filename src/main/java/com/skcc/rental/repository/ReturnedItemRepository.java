package com.skcc.rental.repository;

import com.skcc.rental.domain.ReturnedItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ReturnedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnedItemRepository extends JpaRepository<ReturnedItem, Long> {
}
