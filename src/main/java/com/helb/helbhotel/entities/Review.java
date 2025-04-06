package com.helb.helbhotel.entities;
import lombok.Data;

import java.util.Date;

@Data
public class Review {

    private int rating;

    private  String comment;

    private Date createdDate;
}
