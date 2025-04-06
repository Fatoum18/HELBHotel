package com.helb.helbhotel.entities;
import lombok.Data;

@Data
public class Room {

    private int roomNumber;
    private String type;
    private  double pricePerNight;
    private boolean isAvailable;


}
