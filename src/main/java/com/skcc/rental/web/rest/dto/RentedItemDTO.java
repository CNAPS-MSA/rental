package com.skcc.rental.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skcc.rental.domain.RentedItem} entity.
 */
public class RentedItemDTO implements Serializable {

    private Long id;

    private Long bookId;

    private LocalDate rentedDate;

    private LocalDate dueDate;

    private String bookTitle;


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

    public LocalDate getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(LocalDate rentedDate) {
        this.rentedDate = rentedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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

        RentedItemDTO rentedItemDTO = (RentedItemDTO) o;
        if (rentedItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rentedItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RentedItemDTO{" +
            "id=" + getId() +
            ", bookId=" + getBookId() +
            ", rentedDate='" + getRentedDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", bookTitle='" + getBookTitle() + "'" +
            ", rentalId=" + getRentalId() +
            "}";
    }
}
