package com.helb.helbhotel.entities;
import lombok.Data;

@Data
public class Guest {

    private int guestId;
    private String name;

    private  String email;

    private String phone;
}
