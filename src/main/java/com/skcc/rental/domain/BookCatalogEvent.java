package com.skcc.rental.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCatalogEvent {

    private String title;

    private String description;

    private String author;

    private String publicationDate;

    private String classification;

    private Boolean rented;

    private Long rentCnt;

    private String eventType;

    private Long bookId;

}
