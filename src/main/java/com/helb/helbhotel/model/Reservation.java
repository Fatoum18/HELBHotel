package com.helb.helbhotel.model;

import lombok.Data;

@Data
public class Reservation {
    private String firstName;
    private String lastName;
    private int personCount;
    private boolean smoker;
    private String stayReason;
    private int childCount;
    private boolean valid;

    public String getFullName(){
        return (firstName+" "+lastName).trim();
    }
}