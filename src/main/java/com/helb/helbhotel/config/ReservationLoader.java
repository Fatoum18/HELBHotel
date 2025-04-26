package com.helb.helbhotel.config;
import com.helb.helbhotel.ConfigParse;
import com.helb.helbhotel.model.Reservation;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ReservationLoader {

    public static List<Reservation> loadValidReservations()   {
        try{
            File file = new File(ConfigParse.class.getResource("reservation.csv").toURI());
            List<Reservation> validReservations = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                br.readLine(); // Skip header

                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", -1); // keep empty fields
                    if (fields.length != 6) continue;

                    String lastName = fields[0].trim();
                    String firstName = fields[1].trim();
                    String personStr = fields[2].trim();
                    String smokerStr = fields[3].trim();
                    String reason = fields[4].trim();
                    String childStr = fields[5].trim();

                    // Validate fields
                    if (lastName.isEmpty() || firstName.isEmpty() || personStr.isEmpty() ||
                            smokerStr.isEmpty() || reason.isEmpty() || childStr.isEmpty()) {
                        continue;
                    }

                    try {
                        int personCount = Integer.parseInt(personStr);
                        int childCount = Integer.parseInt(childStr);

                        if (personCount < 1 || personCount > 4) continue;
                        if (childCount < 0 || childCount >= personCount) continue;

                        boolean smoker = smokerStr.equalsIgnoreCase("Fumeur");

                        Reservation reservation = new Reservation();
                        reservation.setLastName(lastName);
                        reservation.setFirstName(firstName);
                        reservation.setPersonCount(personCount);
                        reservation.setSmoker(smoker);
                        reservation.setStayReason(reason);
                        reservation.setChildCount(childCount);
                        reservation.setValid(true);

                        validReservations.add(reservation);
                    } catch (NumberFormatException e) {
                        // Skip invalid numeric values
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return validReservations;
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return  null;
    }
}
