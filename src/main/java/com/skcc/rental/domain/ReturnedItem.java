package com.skcc.rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A ReturnedItem.
 */
@Entity
@Table(name = "returned_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReturnedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "returned_date")
    private LocalDate returnedDate;

    @ManyToOne
    @JsonIgnoreProperties("returnedItems")
    private Rental rental;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public ReturnedItem bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public ReturnedItem returnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
        return this;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public Rental getRental() {
        return rental;
    }

    public ReturnedItem rental(Rental rental) {
        this.rental = rental;
        return this;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReturnedItem)) {
            return false;
        }
        return id != null && id.equals(((ReturnedItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ReturnedItem{" +
            "id=" + getId() +
            ", bookId=" + getBookId() +
            ", returnedDate='" + getReturnedDate() + "'" +
            "}";
    }
}
