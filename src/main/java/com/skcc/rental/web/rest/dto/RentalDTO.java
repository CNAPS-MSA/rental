package com.skcc.rental.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;
import com.skcc.rental.domain.enumeration.RentalStatus;

/**
 * A DTO for the {@link com.skcc.rental.domain.Rental} entity.
 */
public class RentalDTO implements Serializable {

    private Long id;

    private Long userId;

    private RentalStatus rentalStatus;

    private Long lateFee;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Long getLateFee() {
        return lateFee;
    }

    public void setLateFee(Long lateFee) {
        this.lateFee = lateFee;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RentalDTO rentalDTO = (RentalDTO) o;
        if (rentalDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rentalDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RentalDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", rentalStatus='" + getRentalStatus() + "'" +
            ", lateFee=" + getLateFee() +
            "}";
    }
}
