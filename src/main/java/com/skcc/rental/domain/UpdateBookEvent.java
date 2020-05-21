package com.skcc.rental.domain;

public class UpdateBookEvent {

    private Long bookId;
    private String bookStatus;

    public UpdateBookEvent(Long bookId, String bookStatus){
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
