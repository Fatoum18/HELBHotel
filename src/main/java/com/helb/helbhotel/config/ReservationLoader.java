package com.helb.helbhotel.config;

import com.helb.helbhotel.model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ReservationLoader {

    public static List<Reservation> loadValidReservations()   {
        try{
            File file = new File(".."+File.separatorChar+"reservation.csv");
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

    public static boolean removeReservation(Reservation reservationToRemove) {
        try {

            File file = new File(".."+File.separatorChar+"reservation.csv");
            List<String> lines = new ArrayList<>();
            boolean removed = false;

            // Read all lines from the file
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                // Keep the header
                lines.add(br.readLine());

                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", -1);
                    if (fields.length != 6) {
                        lines.add(line); // Keep malformed lines as-is
                        continue;
                    }

                    // Create a reservation from the line for comparison
                    String lastName = fields[0].trim();
                    String firstName = fields[1].trim();
                    String personStr = fields[2].trim();
                    String smokerStr = fields[3].trim();
                    String reason = fields[4].trim();
                    String childStr = fields[5].trim();

                    // Skip if fields are empty
                    if (lastName.isEmpty() || firstName.isEmpty() || personStr.isEmpty() ||
                            smokerStr.isEmpty() || reason.isEmpty() || childStr.isEmpty()) {
                        lines.add(line);
                        continue;
                    }

                    try {
                        int personCount = Integer.parseInt(personStr);
                        int childCount = Integer.parseInt(childStr);
                        boolean smoker = smokerStr.equalsIgnoreCase("Fumeur");

                        // Check if this line matches the reservation to remove
                        if (reservationToRemove.getLastName().equals(lastName) &&
                                reservationToRemove.getFirstName().equals(firstName) &&
                                reservationToRemove.getPersonCount() == personCount &&
                                reservationToRemove.isSmoker() == smoker &&
                                reservationToRemove.getStayReason().equals(reason) &&
                                reservationToRemove.getChildCount() == childCount) {
                            // Skip adding this line (effectively removing it)
                            removed = true;
                        } else {
                            lines.add(line);
                        }
                    } catch (NumberFormatException e) {
                        lines.add(line); // Keep lines with invalid numbers
                    }
                }
            }

            // Only rewrite the file if we found and removed the reservation
            if (removed) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }

            return removed;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
