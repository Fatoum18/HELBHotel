package com.helb.helbhotel.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    public static void addRoomType(RoomType roomType){
        roomTypes.add(roomType);
    }
    public static List<RoomType> getRoomTypes(){
        return new ArrayList<>(roomTypes);
    }

    static void  getRoomCode(String code){

    }



    @Data
    public static  final class Floor {
        private String name;
        private List<Room> rooms;

        public Floor(){

        }
        public  Floor(String name){
            this.name = name;
        }

        public void addRoom(Room room){
            rooms.add(room);
        }

    }

    @Data
    @AllArgsConstructor  // creer un constructeur qui prend tout les attr en argument
    public static  final class Room {
        private String name;
        private String code;
        private String roomTypeCode;

    }

    @Data
    @AllArgsConstructor
    public static  final class RoomType {
        private String code;
        private String name;
        private String color;
    }
}

