package com.skcc.rental.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skcc.rental.domain.OverdueItem} entity.
 */
public class OverdueItemDTO implements Serializable {
    
    private Long id;

    private Long bookId;

    private LocalDate dueDate;


    private Long rentalId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OverdueItemDTO overdueItemDTO = (OverdueItemDTO) o;
        if (overdueItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), overdueItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OverdueItemDTO{" +
            "id=" + getId() +
            ", bookId=" + getBookId() +
            ", dueDate='" + getDueDate() + "'" +
            ", rentalId=" + getRentalId() +
            "}";
    }
}