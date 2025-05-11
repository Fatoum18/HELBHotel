package com.helb.helbhotel;

import com.helb.helbhotel.config.DiscountCodeGenerator;

public class TestTicket {

    public static void main(String[] args) {
        String code = DiscountCodeGenerator.generateDiscountCode(50);
        int percent = DiscountCodeGenerator.validateDiscountCode("3TAT54F19BC");
        System.out.println("Code is "+code+ "  percent="+percent);
    }

}
