package com.helb.helbhotel.entities;
import lombok.Data;

import java.util.Date;

@Data
public class LotteryTicket {

    private String code;

    private Date issueDate;

    private Date expiryDate;
    private boolean isUsed;
}
