package com.skcc.rental;

import com.skcc.rental.domain.OverdueItem;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.RentedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitDb {

    @Autowired
    private  InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    static class InitService {

        @Autowired
        private  EntityManager em;
        public void dbInit1(){

            long userId = 100L;


            Rental rental = Rental.createRental(userId);

            RentedItem rentedItem1 = RentedItem.createRentedItem(111L,"어린왕자", LocalDate.now());
            RentedItem rentedItem2 = RentedItem.createRentedItem(112L,"노인과바다", LocalDate.now());
            RentedItem rentedItem3 = RentedItem.createRentedItem(113L,"이방인", LocalDate.now());
            RentedItem rentedItem4 = RentedItem.createRentedItem(114L,"JPA기본", LocalDate.now());

            rental.rentBooks(rentedItem1);
            rental.rentBooks(rentedItem2);
            rental.rentBooks(rentedItem3);
            rental.rentBooks(rentedItem4);


            rental.returnBooks(rentedItem2);
            rental.overdueBooks(rentedItem1);

            OverdueItem overdueItem = rental.getOverdueItems().stream()
                .filter(overdueItem1 -> overdueItem1.getBookId().equals(111L))
                .findFirst().get();
            rental.releaseOverdue(overdueItem);

            em.persist(rental);
        }



        public void dbInit2(){


        }


    }
}
