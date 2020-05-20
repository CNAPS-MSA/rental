package com.skcc.rental.service.dto;

public class RentalBookDTO {

    private Long bookId;
    private String bookStatus;

    public RentalBookDTO(Long bookId, String bookStatus){
        this.bookId = bookId;
        this.bookStatus = bookStatus;

    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }
}
