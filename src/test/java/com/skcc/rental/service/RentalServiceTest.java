package com.skcc.rental.service;

import com.skcc.rental.adaptor.RentalProducer;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.domain.enumeration.RentalStatus;

import com.skcc.rental.repository.RentalRepository;
import com.skcc.rental.service.impl.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RentalServiceImpl.class)
public class RentalServiceTest {
    private static final Long RENTAL_ID=1L;
    private static final Long BOOK_ID=1L;
    private static final Long USER_ID=1L;

    @Autowired
    RentalService rentalService;

    @MockBean
    RentalRepository rentalRepository;

    @MockBean
    RentalProducer rentalProducer;

    @MockBean
    EntityManager em;

    private Rental rental;


    public static Rental createEntity(EntityManager em) {
        Rental rental = new Rental()
            .userId(USER_ID)
            .rentalStatus(RentalStatus.RENT_UNAVAILABLE)
            .lateFee(30);
        em.persist(rental);
        return rental;
    }

    @BeforeEach
    public void initTest() {
        rental = createEntity(em);
        rental.setId(RENTAL_ID);
        rental.addRentedItem(RentedItem.createRentedItem(BOOK_ID,"어린왕자", LocalDate.now()));
        em.persist(rental);
    }

    @Test
    void do_overdue(){ //연체시키
        //given
        when(rentalRepository.findById(RENTAL_ID)).thenReturn(java.util.Optional.ofNullable(rental));

        rentalService.beOverdueBook(RENTAL_ID, BOOK_ID);

        verify(rentalRepository).save(rental);
    }

    @Test
    void release_overdue(){//연체풀기
       //given
        when(rentalRepository.findByUserId(USER_ID)).thenReturn(java.util.Optional.ofNullable(rental));

        //when
        rentalService.releaseOverdue(USER_ID);

        //then
       verify(rentalRepository).save(rental);


    }


}
