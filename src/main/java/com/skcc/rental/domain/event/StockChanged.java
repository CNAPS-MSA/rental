package com.skcc.rental.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockChanged {

    private Long bookId;
    private String bookStatus;

}
