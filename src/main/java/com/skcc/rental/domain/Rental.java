package com.skcc.rental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import com.skcc.rental.domain.enumeration.RentalStatus;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
    private Long lateFee;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RentedItem> rentedItems = new HashSet<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OverdueItem> overdueItems = new HashSet<>();

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public Rental rentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
        return this;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Long getLateFee() {
        return lateFee;
    }

    public Rental lateFee(Long lateFee) {
        this.lateFee = lateFee;
        return this;
    }

    public void setLateFee(Long lateFee) {
        this.lateFee = lateFee;
    }

    public Set<RentedItem> getRentedItems() {
        return rentedItems;
    }

    public Rental rentedItems(Set<RentedItem> rentedItems) {
        this.rentedItems = rentedItems;
        return this;
    }

    public Rental addRentedItem(RentedItem rentedItem) {
        this.rentedItems.add(rentedItem);
        rentedItem.setRental(this);
        return this;
    }

    public Rental removeRentedItem(RentedItem rentedItem) {
        this.rentedItems.remove(rentedItem);
        rentedItem.setRental(null);
        return this;
    }

    public void setRentedItems(Set<RentedItem> rentedItems) {
        this.rentedItems = rentedItems;
    }

    public Set<OverdueItem> getOverdueItems() {
        return overdueItems;
    }

    public Rental overdueItems(Set<OverdueItem> overdueItems) {
        this.overdueItems = overdueItems;
        return this;
    }

    public Rental addOverdueItem(OverdueItem overdueItem) {
        this.overdueItems.add(overdueItem);
        overdueItem.setRental(this);
        return this;
    }

    public Rental removeOverdueItem(OverdueItem overdueItem) {
        this.overdueItems.remove(overdueItem);
        overdueItem.setRental(null);
        return this;
    }

    public void setOverdueItems(Set<OverdueItem> overdueItems) {
        this.overdueItems = overdueItems;
    }

    public Set<ReturnedItem> getReturnedItems() {
        return returnedItems;
    }

    public Rental returnedItems(Set<ReturnedItem> returnedItems) {
        this.returnedItems = returnedItems;
        return this;
    }

    public Rental addReturnedItem(ReturnedItem returnedItem) {
        this.returnedItems.add(returnedItem);
        returnedItem.setRental(this);
        return this;
    }

    public Rental removeReturnedItem(ReturnedItem returnedItem) {
        this.returnedItems.remove(returnedItem);
        returnedItem.setRental(null);
        return this;
    }

    public void setReturnedItems(Set<ReturnedItem> returnedItems) {
        this.returnedItems = returnedItems;
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

    //최초대여하//
    /**
     *
     * @param userId
     * @param rentedItems
     * @return
     */
    public static Rental createRental(Long userId,RentedItem... rentedItems){
        //처음 대출할때 : 대출카드 생성하고
        Rental rental = new Rental();
        rental.setUserId(userId);
        //대여 가능하게 상태변경
        rental.setRentalStatus(RentalStatus.RENTPOSSIBE);
        //대여목록에 넣기
        for (RentedItem rentalItem:rentedItems) {
            rental.addRentedItem(rentalItem);
        }
        //rental.setOverdueItems(new HashSet<>());
        //rental.setReturnedItems(new HashSet<>());
        rental.setLateFee((long)0);
        return rental;
    }

    //반납 메소드//
    public Rental returnBooks(RentedItem... rentedItems)
    {

        for (RentedItem rentalItem:rentedItems) {
            //대여목록에서 삭제하고
            this.removeRentedItem(rentalItem);
            //대여도서 정보로 반납도서 만들기
            ReturnedItem returnedItem = ReturnedItem.createReturnedItem(rentalItem.getBookId(),LocalDate.now());
            //반납목록에 넣기
            this.addReturnedItem(returnedItem);
        }
        return this;
    }


    //추가대여하기//
    public Rental rentBooks(RentedItem... rentedItems){

        //현재 대여목록 갯수와 대여할 도서 갯수 파악
        Integer currentBookCnt = this.rentedItems.size()+rentedItems.length;
        //5건 이상검토
        if(checkRentalAvailable(currentBookCnt)){
            for (RentedItem rentalItem:rentedItems) {
                //기존 대여목록에 추가하기
                this.addRentedItem(rentalItem);
            }
            return this;

        }else{
            return null;
        }
    }




    //대여 가능 여부 체크 //
    public boolean checkRentalAvailable(Integer newBookCnt){
        if(this.rentalStatus!=RentalStatus.RENTIMPOSSIBLE){

            if(this.rentedItems.size()+newBookCnt >5){
                System.out.println("대출 가능한 도서의 수는 "+( 5- this.getRentedItems().size())+"권 입니다.");
                return false;
            }else{
                return true;
            }
        }else{
            System.out.println("연체 상태입니다.");
            return false;
        }
    }


}
