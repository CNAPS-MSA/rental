package com.skcc.rental.domain;

public class SavePointsEvent {

    Long userId;
    int points;

    public SavePointsEvent(Long userId, int points) {
        this.userId = userId;
        this.points = points;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
