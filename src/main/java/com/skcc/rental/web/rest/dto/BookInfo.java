package com.skcc.rental.web.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BookInfo implements Serializable {
    private Long id;

    private String title;


}
