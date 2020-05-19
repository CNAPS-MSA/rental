package com.skcc.rental.repository;

import com.skcc.rental.domain.OverdueItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OverdueItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OverdueItemRepository extends JpaRepository<OverdueItem, Long> {
}
