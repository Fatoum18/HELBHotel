package com.helb.helbhotel.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

/**
 * A utility class for generating and validating discount codes.
 * This implementation:
 * 1. Creates readable, short discount codes
 * 2. Embeds the discount percentage directly in the code
 * 3. Includes an expiration timestamp
 * 4. Prevents tampering using HMAC authentication
 * 5. Uses URL-safe Base64 encoding for compatibility
 */
public class DiscountCodeGenerator {

    // Secret key for HMAC (should be stored securely in production, e.g., in environment variables)
    private static final String SECRET_KEY = "HotelDiscountSecret123";

    // Constants for code structure
    private static final int CODE_LENGTH = 10;
    private static final long DEFAULT_VALIDITY_DAYS = 30;

    // Character set for the random part of the code (excluding similar-looking characters)
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a discount code with the specified percentage discount.
     *
     * @param discountPercentage The percentage discount (1-99)
     * @return A discount code
     */
    public static String generateDiscountCode(int discountPercentage) {
        // Add default validity days to current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, (int)DEFAULT_VALIDITY_DAYS);
        return generateDiscountCode(discountPercentage, calendar.getTime());
    }

    /**
     * Generates a discount code with the specified percentage discount and expiration.
     *
     * @param discountPercentage The percentage discount (1-99)
     * @param expirationDate When the code expires
     * @return A discount code
     */
    public static String generateDiscountCode(int discountPercentage, Date expirationDate) {
        // Validate discount percentage
        if (discountPercentage < 1 || discountPercentage > 99) {
            throw new IllegalArgumentException("Discount percentage must be between 1 and 99");
        }

        try {
            // Generate a random part (4 characters)
            String randomPart = generateRandomString(4);

            // Format discount as 2 digits
            String discountPart = String.format("%02d", discountPercentage);

            // Expiration timestamp (days since epoch, truncated)
            long expirationDays = expirationDate.getTime() / (1000 * 86400);
            // Take only the least significant 16 bits (covers ~180 years)
            int shortExpiration = (int)(expirationDays & 0xFFFF);
            String expirationHex = Integer.toHexString(shortExpiration).toUpperCase();
            while (expirationHex.length() < 4) {
                expirationHex = "0" + expirationHex;
            }

            // Combine parts to form the payload
            String payload = randomPart + discountPart + expirationHex;

            // Generate HMAC for the payload
            String signature = calculateHMAC(payload);

            // Take first 2 characters of signature for verification
            String signaturePart = signature.substring(0, 2);

            // Combine to form the final code
            String code = payload + signaturePart;

            // Format with dashes for readability
            return formatCodeWithDashes(code);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate discount code", e);
        }
    }

    /**
     * Validates a discount code and returns the discount percentage if valid.
     *
     * @param code The discount code to validate
     * @return The discount percentage
     * @throws IllegalArgumentException if the code is invalid or expired
     */
    public static int validateDiscountCode(String code) {
        try {
            // Remove dashes and spaces if present
            code = code.replaceAll("[-\\s]", "").toUpperCase();

            if (code.length() != 12) {
                throw new IllegalArgumentException("Invalid discount code format");
            }

            // Extract parts
            String payload = code.substring(0, 10);
            String signaturePart = code.substring(10, 12);

            // Verify the signature
            String calculatedSignature = calculateHMAC(payload);
            if (!calculatedSignature.startsWith(signaturePart)) {
                throw new IllegalArgumentException("Invalid discount code (signature mismatch)");
            }

            // Extract discount percentage (characters 5-6)
            int discountPercentage = Integer.parseInt(code.substring(4, 6));

            // Extract expiration (characters 7-10)
            String expirationHex = code.substring(6, 10);
            long expirationDays = Long.parseLong(expirationHex, 16);
            Date expirationDate = new Date(expirationDays * 1000 * 86400);

            // Check if expired
            if (new Date().after(expirationDate)) {
                throw new IllegalArgumentException("Discount code has expired");
            }

            return discountPercentage;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid discount code format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid discount code", e);
        }
    }

    /**
     * Returns the expiration date of a discount code.
     *
     * @param code The discount code
     * @return The expiration date
     * @throws IllegalArgumentException if the code is invalid
     */
    public static Date getExpirationDate(String code) {
        // Remove dashes and spaces if present
        code = code.replaceAll("[-\\s]", "").toUpperCase();

        if (code.length() != 12) {
            throw new IllegalArgumentException("Invalid discount code format");
        }

        try {
            // Extract expiration (characters 7-10)
            String expirationHex = code.substring(6, 10);
            long expirationDays = Long.parseLong(expirationHex, 16);
            return new Date(expirationDays * 1000 * 86400);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid discount code", e);
        }
    }

    /**
     * Calculates an HMAC for the given payload.
     *
     * @param payload The string to sign
     * @return The HMAC signature (hex-encoded)
     */
    private static String calculateHMAC(String payload) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(keySpec);
        byte[] hmacBytes = hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        // Convert bytes to hex without using HexFormat (Java 17+)
        char[] hexChars = new char[hmacBytes.length * 2];
        for (int i = 0; i < hmacBytes.length; i++) {
            int v = hmacBytes[i] & 0xFF;
            hexChars[i * 2] = "0123456789ABCDEF".charAt(v >>> 4);
            hexChars[i * 2 + 1] = "0123456789ABCDEF".charAt(v & 0x0F);
        }
        return new String(hexChars);
    }

    /**
     * Generates a random string of the specified length using the defined character set.
     *
     * @param length The length of the random string
     * @return A random string
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CODE_CHARS.length());
            sb.append(CODE_CHARS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Formats a code with dashes for better readability.
     *
     * @param code The raw code
     * @return The formatted code (e.g., "ABCD-12-3456-78")
     */
    private static String formatCodeWithDashes(String code) {
        return code.substring(0, 4) + "-" +
                code.substring(4, 6) + "-" +
                code.substring(6, 10) + "-" +
                code.substring(10, 12);
    }
}