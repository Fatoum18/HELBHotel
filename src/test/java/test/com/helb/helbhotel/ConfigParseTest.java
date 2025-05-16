package test.com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigParseTest {

    @TempDir
    Path tempDir;

    private File configFile;

    @BeforeEach
    public void setup() {
        // Créer le chemin vers le fichier de configuration temporaire
        configFile = new File(tempDir.toFile().getParent(), ".hconfig");
    }

    // Méthode utilitaire pour écrire du contenu dans un fichier de test
    private void writeConfigFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(content);
        }
    }

    // Méthode pour tester ConfigParse avec le contenu donné
    private void testConfigWithContent(String content, String expectedErrorMessage) {
        try {
            writeConfigFile(content);

            // Redéfinir la méthode pour utiliser notre fichier de test
            TestableConfigParse parser = new TestableConfigParse(configFile);
            parser.load();

            // Si on attend une exception mais qu'elle n'est pas lancée
            if (expectedErrorMessage != null) {
                fail("Exception attendue avec message: " + expectedErrorMessage);
            }
        } catch (IllegalArgumentException e) {
            // Vérifier que l'exception contient le message attendu
            if (expectedErrorMessage == null) {
                fail("Exception non attendue: " + e.getMessage());
            } else {
                assertTrue(e.getMessage().contains(expectedErrorMessage),
                        "Le message d'erreur devrait contenir '" + expectedErrorMessage +
                                "' mais était '" + e.getMessage() + "'");
            }
        } catch (IOException e) {
            fail("Erreur d'E/S inattendue: " + e.getMessage());
        }
    }

    @Test
    public void testValidConfig() {
        String validConfig = "3\nB,Z,E,Z\nE,Z,Z,Z\nE,Z,Z,B";
        testConfigWithContent(validConfig, null); // Aucune exception attendue
    }

    @Test
    public void testEmptyFile() {
        String emptyConfig = "";
        testConfigWithContent(emptyConfig, "Le fichier est vide");
    }

    @Test
    public void testInvalidFloorCount() {
        String invalidFloorCount = "abc\nB,Z,E,Z\nE,Z,Z,Z\nE,Z,Z,B";
        testConfigWithContent(invalidFloorCount, "nombre d'étages");

        String negativeFloorCount = "-2\nB,Z,E,Z\nE,Z,Z,Z\nE,Z,Z,B";
        testConfigWithContent(negativeFloorCount, "nombre d'étages");

        String zeroFloorCount = "0\nB,Z,E,Z\nE,Z,Z,Z\nE,Z,Z,B";
        testConfigWithContent(zeroFloorCount, "nombre d'étages");
    }

    @Test
    public void testNoRoomConfiguration() {
        String noRoomConfig = "3";
        testConfigWithContent(noRoomConfig, "Aucune configuration de chambre");
    }

    @Test
    public void testInvalidCharacter() {
        String invalidCharacter = "3\nB,Z,X,Z\nE,Z,Z,Z\nE,Z,Z,B";
        testConfigWithContent(invalidCharacter, "Caractère invalide");
    }

    @Test
    public void testMissingValue() {
        String missingValue = "3\nB,Z,E,Z\nE,Z,Z,Z\nE,Z,Z,B\nE,Z,Z,";
        testConfigWithContent(missingValue, "Valeur manquante");
    }

    @Test
    public void testIrregularMatrix() {
        String irregularMatrix = "3\nB,Z,E,Z\nE,Z,Z\nE,Z,Z,B";
        testConfigWithContent(irregularMatrix, "Matrice incomplète ou irrégulière");
    }

    // Classe interne pour tester ConfigParse avec un fichier spécifié
    private static class TestableConfigParse extends ConfigParse {
        private static File testFile = null;

        public TestableConfigParse(File testFile) {
            TestableConfigParse.testFile = testFile;
        }

        public static void load() {
            try {
                List<String> lines = readFile(testFile);
                processLines(lines);
            } catch (IOException e) {
                System.err.println("File read error: " + e.getMessage());
            }
        }
    }
}