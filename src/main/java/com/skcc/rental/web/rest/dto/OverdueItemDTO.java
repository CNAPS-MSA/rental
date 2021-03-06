package com.skcc.rental.web.rest.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skcc.rental.domain.OverdueItem} entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OverdueItemDTO implements Serializable {

    private Long id;

    private Long bookId;

    private LocalDate dueDate;

    private String bookTitle;


    private Long rentalId;


}
