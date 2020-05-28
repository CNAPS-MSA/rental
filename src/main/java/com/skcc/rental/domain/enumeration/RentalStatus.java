package com.skcc.rental.domain.enumeration;

/**
 * The RentalStatus enumeration.
 */
public enum RentalStatus {
    RENT_AVAILABLE(0,"대여가능","대여가능상태"),
    RENT_UNAVAILABLE(1,"대여불가","대여불가능상태");

    private Integer id;
    private String title;
    private String description;

    RentalStatus(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
