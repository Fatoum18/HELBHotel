package com.helb.helbhotel;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class ConfigParse {

    public static void main(String[] args) {
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
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Error: The file is empty. First line expected.");
        }

        int numberOfLines;
        try {
            numberOfLines = Integer.parseInt(lines.get(0));
            if (numberOfLines < 1) {
                throw new IllegalArgumentException("Error: The number of floors must contain an integer strictly greater than 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: The number of floors must be a valid integer.");
        }

        // Vérification du nombre réel de lignes fournies après la première ligne
        if (lines.size() - 1 < numberOfLines) {
            throw new IllegalArgumentException("Error : There are " + numberOfLines +
                    " floor(s),but only " + (lines.size() - 1) + " floor(s) have been provided.");
        }

        // Process from line index 1 onwards
        for (int i = 1; i <= numberOfLines && i < lines.size(); i++) {
            String line = lines.get(i).replace(",", "").replaceAll("\\s+", "");
            char rowLabel = (char) ('A' + i - 1);

            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);
                if (ch == ('B') || ch == ('E') || ch == ('L'))  {
                    // Output format: A1B
                    System.out.println(rowLabel + "" + (j + 1) + ch);
                }
                // if Z, skip (implicitly done by not printing)
            }
        }
    }
}
