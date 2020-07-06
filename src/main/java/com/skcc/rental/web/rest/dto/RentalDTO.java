package com.skcc.rental.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;
import com.skcc.rental.domain.enumeration.RentalStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link com.skcc.rental.domain.Rental} entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RentalDTO implements Serializable {

    private Long id;

    private Long userId;

    private RentalStatus rentalStatus;

    private int lateFee;




}
