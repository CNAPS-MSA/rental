package com.skcc.rental.web.rest.dto;

import java.io.Serializable;

public class LatefeeDTO implements Serializable {
    Long userId;
    int latefee;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getLatefee() {
        return latefee;
    }

    public void setLatefee(int latefee) {
        this.latefee = latefee;
    }
}
