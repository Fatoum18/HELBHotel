package com.helb.helbhotel.config;

import java.io.*;
import java.util.*;

public class ConfigParse {


    public static void load()  throws IllegalArgumentException, IOException {

            File file = new File(".." + File.separatorChar + ".hconfig");
            List<String> lines = readFile(file);
            processLines(lines);

    }

    protected static List<String> readFile(File filename) throws IOException {
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

    protected static void processLines(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le fichier est vide. Première ligne attendue.");
        }

        // Validation du nombre d'étages
        int floorCount;
        try {
            floorCount = Integer.parseInt(lines.get(0));
            if (floorCount < 1) {
                throw new IllegalArgumentException("Erreur : Le nombre d'étages doit être un entier strictement supérieur à 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erreur : Le nombre d'étages doit être un entier valide.");
        }

        // Vérification qu'il y a au moins une ligne de configuration après le nombre d'étages
        if (lines.size() <= 1) {
            throw new IllegalArgumentException("Erreur : Aucune configuration de chambre trouvée.");
        }

        // Vérification de la matrice de chambres
        int totalLine = lines.size();

        // Validation de la structure et du contenu des lignes de configuration
        int expectedColumns = -1;

        for (int i = 1; i < totalLine; i++) {
            String line = lines.get(i).replace(" ", "");
            String[] rooms = line.split(",");

            // Vérifier que chaque ligne a le même nombre de colonnes
            if (expectedColumns == -1) {
                expectedColumns = rooms.length;
            } else if (rooms.length != expectedColumns) {
                throw new IllegalArgumentException("Erreur : Matrice incomplète ou irrégulière. Chaque ligne doit avoir le même nombre de colonnes.");
            }

            // Vérifier les valeurs vides à la fin d'une ligne (comme dans E,Z,Z,)
            for (String room : rooms) {
                if (room.isEmpty()) {
                    throw new IllegalArgumentException("Erreur : Valeur manquante dans la matrice. Assurez-vous que chaque élément est B, E, L ou Z.");
                }

                if (!room.equals("B") && !room.equals("E") && !room.equals("L") && !room.equals("Z")) {
                    throw new IllegalArgumentException("Erreur : Caractère invalide dans la matrice. Seuls B, E, L et Z sont autorisés.");
                }
            }
        }

        // Traitement des étages après validation
        for (int i = 0; i < floorCount; i++) {
            char floorPrefix = (char) ('A' + i);
            ConfigStore.Floor floor = new ConfigStore.Floor(floorPrefix + "");

            System.out.println(" ====== floor " + floorPrefix + "======");
            int roomCount = 1;

            for (int lineCount = 1; lineCount < totalLine; lineCount++) {
                String line = lines.get(lineCount).replace(",", "").replaceAll("\\s+", "");

                for (int j = 0; j < line.length(); j++) {
                    char ch = line.charAt(j);
                    if (ch == 'B' || ch == 'E' || ch == 'L') {
                        // Output format: A1B
                        System.out.print(floorPrefix + "" + roomCount + ch);
                        floor.addRoom(new ConfigStore.Room(floorPrefix + "", roomCount, ch + ""));
                        roomCount++;
                    } else if (ch == 'Z') {
                        System.out.print("Z");
                        floor.addRoom(new ConfigStore.Room(floorPrefix + "", roomCount, ch + ""));
                    } else {
                        // Caractère invalide, cette partie est maintenant gérée en amont par la validation
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
            ConfigStore.addFloor(floor);
        }
    }
}