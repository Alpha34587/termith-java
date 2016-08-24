package module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
/**
 * Created by Simon Meoni on 16/08/16.
 */
public class FilesUtilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtilities.class.getName());

    public static String createTemporaryFolder(String path) throws IOException {
        Path tempDir = Files.createTempDirectory(path);
        return tempDir.toString();
    }

    public static void createFiles(String path, Map<String, StringBuffer> extractedText, String extension) {
        extractedText.forEach((filename, content) -> {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + "/" + filename + "." + extension))){
                LOGGER.info("write file: " + path + "/" + filename + "." + extension);
                writer.write(String.valueOf(content));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
