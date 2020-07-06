package com.skcc.rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A OverdueItem.
 */
@Entity
@Table(name = "overdue_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@ToString
public class OverdueItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "book_title")
    private String bookTitle;

    @ManyToOne
    @JsonIgnoreProperties("overdueItems")
    private Rental rental;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    //생성 메소드
    public static OverdueItem createOverdueItem(Long bookId, String bookTitle, LocalDate dueDate){
        OverdueItem overdueItem = new OverdueItem();
        overdueItem.setBookId(bookId);
        overdueItem.setBookTitle(bookTitle);
        overdueItem.setDueDate(dueDate);

        return overdueItem;
    }

    public OverdueItem bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public OverdueItem dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public OverdueItem bookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public OverdueItem rental(Rental rental) {
        this.rental = rental;
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OverdueItem)) {
            return false;
        }
        return id != null && id.equals(((OverdueItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }



}
