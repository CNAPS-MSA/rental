package com.skcc.rental.web.rest.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skcc.rental.domain.ReturnedItem} entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ReturnedItemDTO implements Serializable {

    private Long id;

    private Long bookId;

    private LocalDate returnedDate;

    private String bookTitle;


    private Long rentalId;


}
