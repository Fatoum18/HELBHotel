package com.helb.helbhotel.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigStore {
    private static final List<Floor> floors = new ArrayList<>();

    private  static final  List<RoomType> roomTypes = new ArrayList<>();

    static {
        initializeRoomTypes();
    }
    private  ConfigStore(){

    }

    private static void initializeRoomTypes(){
        roomTypes.add(new RoomType("L","Luxe","#dbcce3"));
        roomTypes.add(new RoomType("B","Business","#cce1f2"));
        roomTypes.add(new RoomType("E","Economic","#f9e4b7"));
    }
    public static void addFloor(Floor floor){
        floors.add(floor);
    }
    public static List<Floor> getFloors(){
        return new ArrayList<>(floors);
    }
    public static Room findRoomByCode(String code){
        Pattern pattern = Pattern.compile("^([A-Za-z]).*");
        Matcher matcher = pattern.matcher(code);
        if(matcher.find()){
            String floorCode = matcher.group(1);
            for(Floor floor : floors){
                if(floor.getName().equals(floorCode)){
                    for (Room room : floor.getRooms()){
                        if(room.getName().equals(code)){
                             return room;
                        }
                    }
                }
            }
        }
        return  null;

    }
    public static void updateRoomStatus(String code, boolean isBusy){
        Room room = findRoomByCode(code);

        if(room!=null){
            room.setBusy(isBusy);
        }

    }

    public static void addRoomType(RoomType roomType){
        roomTypes.add(roomType);
    }
    public static List<RoomType> getRoomTypes(){
        return new ArrayList<>(roomTypes);
    }


    public static List<Room> getAllRooms(){

        List<Room> rooms = new ArrayList<>();
        List<Floor> floorList =  getFloors();
            floorList.forEach(floor -> {
                rooms.addAll(floor.getRooms());
            });

            return  rooms;

    }

    static void  getRoomCode(String code){

    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  final class Floor {
        private String name;
        private List<Room> rooms;

        public  Floor(String name){
                this.name = name;
                this.rooms = new ArrayList<>();
        }

        public void addRoom(Room room){
            rooms.add(room);
        }

        public String getFullName(){
                return  "Floor: "+name;
        }

    }

    @Data
    @AllArgsConstructor  // creer un constructeur qui prend tout les attr en argument
    public static  final class Room {
        private String floorPrefix;
        private int roomNumber;
        private String roomTypeCode;

        private boolean isBusy;

        public Room(String floorPrefix, int roomNumber, String roomTypeCode) {
            this.floorPrefix = floorPrefix;
            this.roomNumber = roomNumber;
            this.roomTypeCode = roomTypeCode;
        }

        public String getName(){
            if(!roomTypeCode.equals("Z"))
                return floorPrefix + roomNumber + roomTypeCode;
            return roomTypeCode;
        }

    }

    @Data
    @AllArgsConstructor
    public static  final class RoomType {
        private String code;
        private String name;
        private String color;
    }


    public static String getRoomColor(Room room){

        if(room.isBusy){
            return "#F6CDC9";
        }
        return ConfigStore.getRoomTypes().stream()
                .filter(rt -> rt.getCode().equals(room.getRoomTypeCode()))
                .findFirst()
                .map(RoomType::getColor)
                .orElse("#fff");
    }
}

