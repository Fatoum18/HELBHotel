package com.helb.helbhotel.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountCodeGenerator {
    private static final String CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789-";
    private static final int CODE_LENGTH = 10;
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SECRET = "HELBHotel"; // Change this!

    private static final List<Integer> DISCOUNTS = Arrays.asList(100, 50, 25);
    private static final SecureRandom random = new SecureRandom();

    // Generate discount code
    public static String generateCode(int discount) {
        int index = DISCOUNTS.indexOf(discount);
        if (index == -1) throw new IllegalArgumentException("Invalid discount");

        byte[] hmac = hmac(index + ":" + random.nextInt(10000));
        long hash = bytesToLong(hmac);

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int pos = (int) (hash % CHARSET.length());
            code.append(CHARSET.charAt(pos));
            hash /= CHARSET.length();
        }

        storeCode(code.toString(), discount);
        return code.toString();
    }

    // Decode is not possible without mapping (HMAC is not reversible).
    // Instead, you’d store the generated code and the associated discount in a secure DB.

    // HMAC utility
    private static byte[] hmac(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(SECRET.getBytes(), HMAC_ALGO));
            return mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("HMAC error", e);
        }
    }

    private static long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < 8 && i < bytes.length; i++) {
            result = (result << 8) | (bytes[i] & 0xff);
        }
        return Math.abs(result);
    }

    // Example: reverse mapping with DB or Map
    private static final Map<String, Integer> codeToDiscountMap = new HashMap<>();

    public static void storeCode(String code, int discount) {
        codeToDiscountMap.put(code, discount);
    }

    public static Integer decode(String code) {
        return codeToDiscountMap.get(code);
    }


}
