package com.skcc.rental.service.impl;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.enumeration.RentalStatus;
import com.skcc.rental.repository.RentalRepository;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class RentalServiceImplTest {


    @Autowired
    private RentalServiceImpl rentalService;

    @Autowired
    private RentalRepository rentalRepository;

    @org.junit.Test
    @Transactional
    public void rentBooks() {
        long userId = 111L;
        List<Long> bookId = new ArrayList();
        bookId.add(123L);
        bookId.add(234L);
        bookId.add(464L);

        System.out.println("test start");
        Rental rental = this.rentalService.rentBooks(userId,bookId);

        System.out.println(rental.toString());
        Rental rental1 = this.rentalRepository.findByUserId(userId).get();
        System.out.println(rental1.toString());

        assertEquals("빌린도서", RentalStatus.RENTPOSSIBE,rental1.getRentalStatus());
        System.out.println(rental1.getRentedItems().toString());
        assertEquals("빌린도서",3,rental1.getRentedItems().size());
    }

    @Test
    @Transactional
    public void returnBooks() {
        long userId = 111L;
        List<Long> bookId = new ArrayList();
        bookId.add(123L);
        bookId.add(234L);
        bookId.add(464L);

        System.out.println("------책을 3권 빌린다 --------");
        Rental rental = this.rentalService.rentBooks(userId,bookId);
        System.out.println("------책을 1권 반납 --------");
        Rental rental2 = this.rentalService.returnBooks(userId, bookId.get(1));

        assertEquals("도서카드상태", RentalStatus.RENTPOSSIBE,rental2.getRentalStatus());
        assertEquals("빌린도서",2,rental2.getRentedItems().size());
        assertEquals("반납도서",1,rental2.getReturnedItems().size());

    }
}
