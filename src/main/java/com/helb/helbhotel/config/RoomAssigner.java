package com.helb.helbhotel.config;

import com.helb.helbhotel.model.AssignmentMode;
import com.helb.helbhotel.model.Reservation;

import java.util.*;
import java.util.stream.Collectors;

public class RoomAssigner {


    public static class PotentialAssign {
        private final Reservation reservation;
        private final ConfigStore.Room room;

        public PotentialAssign(Reservation reservation, ConfigStore.Room room) {
            this.reservation = reservation;
            this.room = room;
        }

        public Reservation getReservation() {
            return reservation;
        }

        public ConfigStore.Room getRoom() {
            return room;
        }
    }

    public static List<PotentialAssign> assignRooms(List<Reservation> reservations,
                                             AssignmentMode mode,
                                             List<ConfigStore.Room> availableRooms) {

        List<ConfigStore.RoomType> roomTypes = ConfigStore.getRoomTypes();
        List<PotentialAssign> assignments = new ArrayList<>();
        List<ConfigStore.Room> remainingRooms = new ArrayList<>(availableRooms);

        // Create a map of room type codes to room types for quick lookup
        Map<String, ConfigStore.RoomType> roomTypeMap = roomTypes.stream()
                .collect(Collectors.toMap(ConfigStore.RoomType::getCode, rt -> rt));

        for (Reservation reservation : reservations) {
            if (!reservation.isValid()) {
                continue; // Skip invalid reservations
            }

            ConfigStore.Room assignedRoom = null;

            switch (mode) {
                case RANDOM_ASSIGNMENT:
                    assignedRoom = randomAssignment(remainingRooms);
                    break;
                case QUIET_ZONE:
                    assignedRoom = quietZoneAssignment(reservation, remainingRooms, assignments);
                    break;
                case STAY_PURPOSE:
                    assignedRoom = stayPurposeAssignment(reservation, remainingRooms, roomTypeMap);
                    break;
                case SEQUENTIAL_ASSIGNMENT:
                    assignedRoom = sequentialAssignment(reservation, remainingRooms, roomTypeMap);
                    break;
            }

            if (assignedRoom != null) {
                assignments.add(new PotentialAssign(reservation, assignedRoom));
                remainingRooms.remove(assignedRoom);
            }
        }

        return assignments;
    }

    private static ConfigStore.Room randomAssignment(List<ConfigStore.Room> availableRooms) {
        if (availableRooms.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return availableRooms.get(random.nextInt(availableRooms.size()));
    }

    private static ConfigStore.Room quietZoneAssignment(Reservation reservation, List<ConfigStore.Room> availableRooms,
                                     List<PotentialAssign> existingAssignments) {
        // Filter rooms based on smoker and child requirements
        List<ConfigStore.Room> filteredRooms = availableRooms.stream()
                .filter(room -> {
                    // Smokers should have windows (assuming rooms with even numbers have windows)
                    if (reservation.isSmoker()) {
                        return room.getRoomNumber() % 2 == 0;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (reservation.getChildCount() > 0) {
            // For reservations with children, we need to ensure neighbors don't have non-child reservations
            // This is a simplified version - in reality you'd need room layout data
            filteredRooms = filteredRooms.stream()
                    .filter(room -> {
                        // Check if adjacent rooms (by number) have non-child reservations
                        int roomNum = room.getRoomNumber();
                        for (PotentialAssign assignment : existingAssignments) {
                            ConfigStore.Room assignedRoom = assignment.getRoom();
                            int assignedNum = assignedRoom.getRoomNumber();
                            if (Math.abs(assignedNum - roomNum) == 1 &&
                                    assignment.getReservation().getChildCount() == 0) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        // If no rooms meet the strict criteria, fall back to any available room
        if (filteredRooms.isEmpty()) {
            return randomAssignment(availableRooms);
        }

        return randomAssignment(filteredRooms);
    }

    private static ConfigStore.Room stayPurposeAssignment(Reservation reservation, List<ConfigStore.Room> availableRooms,
                                                   Map<String, ConfigStore.RoomType> roomTypeMap) {
        String desiredTypeCode = determineRoomType(reservation, roomTypeMap);

        List<ConfigStore.Room> suitableRooms = availableRooms.stream()
                .filter(room -> room.getRoomTypeCode().equals(desiredTypeCode))
                .collect(Collectors.toList());

        if (suitableRooms.isEmpty()) {
            // Fallback to any available room
            return randomAssignment(availableRooms);
        }

        return randomAssignment(suitableRooms);
    }

    private static ConfigStore.Room sequentialAssignment(Reservation reservation, List<ConfigStore.Room> availableRooms,
                                      Map<String, ConfigStore.RoomType> roomTypeMap) {
        String desiredTypeCode = determineRoomType(reservation, roomTypeMap);

        // Sort rooms by floor and room number (ascending)
        List<ConfigStore.Room> sortedRooms = availableRooms.stream()
                .sorted(Comparator.comparing(ConfigStore.Room::getFloorPrefix)
                        .thenComparingInt(ConfigStore.Room::getRoomNumber))
                .collect(Collectors.toList());

        // Find first suitable room of the desired type
        Optional<ConfigStore.Room> foundRoom = sortedRooms.stream()
                .filter(room -> room.getRoomTypeCode().equals(desiredTypeCode))
                .findFirst();

        if (foundRoom.isPresent()) {
            return foundRoom.get();
        }

        // Fallback to first available room if no room of desired type
        if (!sortedRooms.isEmpty()) {
            return sortedRooms.get(0);
        }

        return null;
    }

    private static String determineRoomType(Reservation reservation, Map<String, ConfigStore.RoomType> roomTypeMap) {
        if ("Affaire".equals(reservation.getStayReason())) {
            // Find Business room type code
            return roomTypeMap.values().stream()
                    .filter(rt -> "Business".equals(rt.getName()))
                    .findFirst()
                    .map(ConfigStore.RoomType::getCode)
                    .orElse("E"); // Fallback to Economic
        } else if ("Tourisme".equals(reservation.getStayReason()) ||
                "Autre".equals(reservation.getStayReason())) {
            if (!reservation.isSmoker() && reservation.getChildCount() == 0) {
                // Find Luxe room type code
                return roomTypeMap.values().stream()
                        .filter(rt -> "Luxe".equals(rt.getName()))
                        .findFirst()
                        .map(ConfigStore.RoomType::getCode)
                        .orElse("E"); // Fallback to Economic
            } else {
                // Find Economic room type code
                return roomTypeMap.values().stream()
                        .filter(rt -> "Economic".equals(rt.getName()))
                        .findFirst()
                        .map(ConfigStore.RoomType::getCode)
                        .orElse("E");
            }
        }

        // Default to Economic
        return "E";
    }
}