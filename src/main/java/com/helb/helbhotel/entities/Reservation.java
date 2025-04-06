package com.helb.helbhotel.entities;
import lombok.Data;

import java.util.Date;

@Data
public class Reservation {

    private Date checkInDate;
    private  Date checkOutDate;
    private double totalAmount;
    private String status;
}
