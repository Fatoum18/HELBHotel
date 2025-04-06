package com.helb.helbhotel.entities;
import lombok.Data;

import java.util.Date;

@Data
public class DiscountCode {

    private String code;
    private  double discountPercentage;
    private Date expiryDate;
    private boolean isUsed;
}
