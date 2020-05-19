package com.skcc.rental.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skcc.rental.domain.ReturnedItem} entity.
 */
public class ReturnedItemDTO implements Serializable {
    
    private Long id;

    private Long bookId;

    private LocalDate returnedDate;


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

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
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

        ReturnedItemDTO returnedItemDTO = (ReturnedItemDTO) o;
        if (returnedItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), returnedItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReturnedItemDTO{" +
            "id=" + getId() +
            ", bookId=" + getBookId() +
            ", returnedDate='" + getReturnedDate() + "'" +
            ", rentalId=" + getRentalId() +
            "}";
    }
}
