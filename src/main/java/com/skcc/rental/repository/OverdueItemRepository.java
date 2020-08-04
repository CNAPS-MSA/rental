package com.skcc.rental.repository;

import com.skcc.rental.domain.OverdueItem;

import com.skcc.rental.domain.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.net.http.HttpHeaders;

/**
 * Spring Data  repository for the OverdueItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OverdueItemRepository extends JpaRepository<OverdueItem, Long> {
    Page<OverdueItem> findByRental(Rental rental, Pageable pageable);
}
