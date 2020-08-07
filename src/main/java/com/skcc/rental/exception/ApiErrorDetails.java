package com.skcc.rental.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ApiErrorDetails {

    private String message;
    private int code;
    private LocalDateTime timeStamp;


}
