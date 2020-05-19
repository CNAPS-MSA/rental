package com.skcc.rental.repository;

import com.skcc.rental.domain.Rental;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Rental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental> findByUserId(Long userId);
}
