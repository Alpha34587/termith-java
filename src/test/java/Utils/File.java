package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class File {

    public static StringBuffer ReadFile(Path path) throws IOException {
        return new StringBuffer(String.join("\n", Files.readAllLines(path)));
    }
}
