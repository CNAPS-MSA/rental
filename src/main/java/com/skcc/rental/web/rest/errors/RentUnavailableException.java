package com.skcc.rental.web.rest.errors;

public class RentUnavailableException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RentUnavailableException(){

    }

    public RentUnavailableException(String message){
        super(message);
    }
}
