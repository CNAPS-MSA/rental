package com.skcc.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SavePointsEvent {

    Long userId;
    int points;


}
