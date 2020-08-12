package com.skcc.rental.domain;

import com.skcc.rental.domain.enumeration.RentalStatus;
import com.skcc.rental.web.rest.errors.RentUnavailableException;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status")
    private RentalStatus rentalStatus;

    @Column(name = "late_fee")
    private int lateFee;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true) //고아 객체 제거 -> rental에서 컬렉션의 객체 삭제시, 해당 컬렉션의 entity삭제
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RentedItem> rentedItems = new HashSet<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OverdueItem> overdueItems = new HashSet<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReturnedItem> returnedItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Rental userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Rental rentalStatus(RentalStatus rentalStatus){
        this.rentalStatus = rentalStatus;
        return  this;
    }


    public Rental addRentedItem(RentedItem rentedItem) {
        this.rentedItems.add(rentedItem);
        rentedItem.setRental(this);
        return this;
    }

    public Rental removeRentedItem(RentedItem rentedItem) {
        this.rentedItems.remove(rentedItem);
        //rentedItem.setRental(null);
        return this;
    }

    public Rental addOverdueItem(OverdueItem overdueItem) {
        this.overdueItems.add(overdueItem);
        overdueItem.setRental(this);
        return this;
    }


    public Rental removeOverdueItem(OverdueItem overdueItem) {
        this.overdueItems.remove(overdueItem);
       // overdueItem.setRental(null);
        return this;
    }



    public Rental addReturnedItem(ReturnedItem returnedItem) {
        this.returnedItems.add(returnedItem);
        returnedItem.setRental(this);
        return this;
    }
    public Rental removeReturnedItem(ReturnedItem returnedItem) {
        this.returnedItems.remove(returnedItem);
      //  returnedItem.setRental(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        return id != null && id.equals(((Rental) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Rental{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", rentalStatus='" + getRentalStatus() + "'" +
            ", lateFee=" + getLateFee() +
            "}";
    }
    //최초 대여 시//
    /**
     * 대여카드 생성
     *
     * @param userId
     * @return
     */
    public static Rental createRental(Long userId) {
        Rental rental = new Rental();
        rental.setUserId(userId);
        //대여 가능하게 상태 변경
        rental.setRentalStatus(RentalStatus.RENT_AVAILABLE);
        rental.setLateFee(0);
        return rental;
    }

    /**
     * 대여 불가 처리
     *
     * @return
     */
    public Rental makeRentUnable() {
        this.setRentalStatus(RentalStatus.RENT_UNAVAILABLE);
        this.setLateFee(this.getLateFee() + 30); //연체시 연체비 30포인트 누적
        return this;
    }

    /**
     * 대여하기
     *
     * @param bookid
     * @param title
     * @return
     */
    public Rental rentBook(Long bookid, String title) {
        this.addRentedItem(RentedItem.createRentedItem(bookid, title, LocalDate.now()));
        return this;
    }

    /**
     * 반납하기
     *
     * @param bookId
     * @return
     */
    public Rental returnbook(Long bookId) {
        RentedItem rentedItem = this.rentedItems.stream().filter(item -> item.getBookId().equals(bookId)).findFirst().get();
        this.addReturnedItem(ReturnedItem.createReturnedItem(rentedItem.getBookId(), rentedItem.getBookTitle(), LocalDate.now()));
        this.removeRentedItem(rentedItem);
        return this;
    }

    /**
     * 연체처리
     *
     * @param bookId
     * @return
     */
    public Rental overdueBook(Long bookId) {
        RentedItem rentedItem = this.rentedItems.stream().filter(item -> item.getBookId().equals(bookId)).findFirst().get();
        this.addOverdueItem(OverdueItem.createOverdueItem(rentedItem.getBookId(), rentedItem.getBookTitle(), rentedItem.getDueDate()));
        this.removeRentedItem(rentedItem);
        return this;
    }

    /**
     * 연체된 책 반납
     *
     * @param bookId
     * @return
     */
    public Rental returnOverdueBook(Long bookId) {
        OverdueItem overdueItem = this.overdueItems
            .stream().filter(item -> item.getBookId().equals(bookId)).findFirst().get();
        this.addReturnedItem(ReturnedItem.createReturnedItem(overdueItem.getBookId(), overdueItem.getBookTitle(), LocalDate.now()));
        this.removeOverdueItem(overdueItem);
        return this;
    }

    //연체 상태 해제//
    public Rental releaseOverdue(int lateFee) {
        this.setLateFee(this.lateFee - lateFee);
        this.setRentalStatus(RentalStatus.RENT_AVAILABLE);
        return this;
    }

    //대여 가능 여부 체크 //
    public void checkRentalAvailable() throws RentUnavailableException {
        if(this.rentalStatus.equals(RentalStatus.RENT_UNAVAILABLE ) || this.getLateFee()!=0) throw new RentUnavailableException("연체 상태입니다. 연체료를 정산 후, 도서를 대여하실 수 있습니다.");
        if(this.getRentedItems().size()>=5) throw new RentUnavailableException("대출 가능한 도서의 수는 "+( 5- this.getRentedItems().size())+"권 입니다.");

    }


    public Rental lateFee(int lateFee) {
        this.lateFee = lateFee;
        return this;
    }

}
