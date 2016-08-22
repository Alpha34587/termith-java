package module;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import thread.TermithXmlInjector;

/**
 * Created by Simon Meoni on 16/08/16.
 */
public class FilesUtilities {
    private static Logger LOGGER = Logger.getLogger(FilesUtilities.class.getName());

    public static String createTemporaryFolder(String path) throws IOException {
        Path tempDir = Files.createTempDirectory(path);
        return tempDir.toString();
    }

    public static void createFiles(String path, Map<String, StringBuffer> extractedText, String extension) {
        extractedText.forEach((filename, content) -> {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + "/" + filename + "." + extension))){
                LOGGER.log(Level.INFO,"write file: " + path + "/" + filename + "." + extension);
                writer.write(String.valueOf(content));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
