package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static String readFileToString(String fileString) {
        String content = fileString + " not found";
        try {
            content = new String(Files.readAllBytes(Paths.get(fileString)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

}
