package org.atilf.module.tools;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import static org.atilf.models.TermithIndex.outputPath;

/**
 * FileUtilities group several static method in order to manipulate the file system during the different process of
 * TermithText
 * @author Simon Meoni
 * Created on 16/08/16.
 */
public class FilesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtils.class.getName());

    /**
     * Create temporary folder with a name
     * @param path the name of the temporary folder
     * @return return the path created
     * @throws IOException throw an exception if the name of the path is incorrect
     */
    public static String createTemporaryFolder(String path) throws IOException {
        Path tempDir = Files.createTempDirectory(path);
        return tempDir.toString();
    }

    /**
     * create files with the content of a StringBuilder
     * @param path the path of the folder
     * @param corpus the corpus with the name of the file and his content
     * @param extension the extension expected of the created file
     */
    public static void createFiles(String path, Map<String, StringBuilder> corpus, String extension) {
        corpus.forEach((filename, content) -> {
            try (BufferedWriter writer =
                         Files.newBufferedWriter(Paths.get(path + "/" + filename + "." + extension))){
                LOGGER.debug("write file: " + path + "/" + filename + "." + extension);
                writer.write(String.valueOf(content));
                writer.close();

            } catch (IOException e) {
                LOGGER.error("error during copying some files",e);
            }

        });
    }

    public static StringBuilder readFile(Path path){
        StringBuilder file = null;
        try {
                    file = new StringBuilder(String.join("\n", Files.readAllLines(path)));
        } catch (IOException e) {
            LOGGER.error("cannot read file : ",e);

        }
        return file;
    }

    public static String nameNormalizer(String path){
        String name = Paths.get(path).getFileName().toString();
        return name.split("\\.")[0];
    }

    public static void exportTerminology(TermithIndex termithIndex) {
        try {
            LOGGER.debug("copying tbx and json terminology ...");
            Files.copy(termithIndex.getTerminologies().get(0),
                    Paths.get(outputPath+"/terminology.tbx"),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy(termithIndex.getTerminologies().get(1),
                    Paths.get(outputPath+"/terminology.json"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("cannot copy terminologies",e);
        }
    }

    public static Path writeObject(Object o,Path workingPath) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        Path path = Paths.get(workingPath + "/" + UUID.randomUUID().toString());
        try {
            fos = new FileOutputStream(path.toString());
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.flush();
            oos.reset();
            oos.close();
        } catch (IOException e) {
            LOGGER.error("could not write object",e);
        }
        finally {
            if (fos != null){
                fos.close();
            }
        }
        return path;
    }

    public static Object readObject(Path filePath){

        FileInputStream fis = null;
        ObjectInputStream in = null;
        Object o = null;
        try {
            fis = new FileInputStream(new File(filePath.toString()));
            in = new ObjectInputStream(fis);
            o = in.readObject();
            in.close();
        }
        catch (IOException e) {
            LOGGER.error("could not open file",e);

        }
        catch (ClassNotFoundException e) {
            LOGGER.error("could import object",e);
        }
        finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("could not close object",e);
                }
            }
        }
        return o;
    }

    public static Path folderPathResolver(String path){
        return Paths.get(path).normalize();
    }
    
}