package com.helb.helbhotel;
import com.helb.helbhotel.config.ConfigStore;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class ConfigParse {

    public static void main(String[] args) {
        ConfigParse.load();
    }

    public  static void load(){
        try {
            File file = new File(ConfigParse.class.getResource(".hconfig").toURI());
            List<String> lines = readFile(file); // Your file name here
            processLines(lines);
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> readFile(File filename) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                lines.add(line.trim());
            }
        }
        reader.close();
        return lines;
    }

    private static void processLines(List<String> lines) {
        int totalLine = lines.size();
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Error: The file is empty. First line expected.");
        }

        int floorCount;
        try {
            floorCount = Integer.parseInt(lines.get(0));
            if (floorCount < 1) {
                throw new IllegalArgumentException("Error: The number of floors must contain an integer strictly greater than 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: The number of floors must be a valid integer.");
        }

        // Vérification du nombre réel de lignes fournies après la première ligne
        if (lines.size()<=1) {
            throw new IllegalArgumentException("Error : No room configuration found");
        }

        // Process from line index 1 onwards
        for (int i = 0; i <floorCount; i++) {

            char floorPrefix = (char) ('A' + i);
            ConfigStore.Floor floor = new ConfigStore.Floor(floorPrefix + "");

            System.out.println(" ====== floor "+floorPrefix+"======");
            int roomCount = 1;
            for  (int lineCount = 1; lineCount<totalLine; lineCount++){

                String line = lines.get(lineCount).replace(",", "").replaceAll("\\s+", "");

                for (int j = 0; j < line.length(); j++) {
                    char ch = line.charAt(j);
                    if (ch == ('B') || ch == ('E') || ch == ('L'))  {
                        // Output format: A1B
                        System.out.print(floorPrefix + "" + roomCount + ch);
                        floor.addRoom(new ConfigStore.Room(floorPrefix+"",roomCount,ch+""));
                        roomCount++;
                    }else if (ch == ('Z'))  {
                        System.out.print("Z");
                        floor.addRoom(new ConfigStore.Room(floorPrefix+"",roomCount,ch+""));
                    }
                    System.out.print(" ");

                    // if Z, skip (implicitly done by not printing)
                }

                System.out.println();
            }
            ConfigStore.addFloor(floor);

        }
    }
}
