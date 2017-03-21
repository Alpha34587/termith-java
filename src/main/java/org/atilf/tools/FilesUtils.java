package org.atilf.tools;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    static String createTemporaryFolder(String path) throws IOException {
        Path tempDir = Files.createTempDirectory(path);
        return tempDir.toString();
    }

    /**
     * create files with the content of a StringBuilder
     * @param path the path of the folder
     * @param corpus the corpus with the name of the file and his content
     * @param extension the extension expected of the created file
     */
    static void createFiles(String path, Map<String, StringBuilder> corpus, String extension) {
        corpus.forEach((filename, content) -> {
            try (BufferedWriter writer =
                         Files.newBufferedWriter(Paths.get(path + "/" + filename + "." + extension))){
                LOGGER.debug("write file: " + path + "/" + filename + "." + extension);
                writer.write(String.valueOf(content));
                writer.close();

            } catch (IOException e) {
                LOGGER.error("error during creating some files",e);
            }

        });
    }

    /**
     * read a file
     * @param path the path of the file
     * @return a StringBuilder with the file content
     */
    public static StringBuilder readFile(Path path){
        StringBuilder file = null;
        try {
            file = new StringBuilder(String.join("\n", Files.readAllLines(path)));
        } catch (IOException e) {
            LOGGER.error("cannot read file : ",e);

        }
        return file;
    }

    /**
     * normalize name of a file remove extension and keep only the name
     * @param path the path of the file
     * @return the normalized name
     */
    public static String nameNormalizer(String path){
        String name = Paths.get(path).getFileName().toString();
        return name.split("\\.")[0];
    }


    /**
     * export java object to a file
     * @param o the java object
     * @param workingPath the path of the working directory
     * @return the path of the file
     * @throws IOException thrown an exception if the object is not writable
     */
    public static Path writeObject(Object o,Path workingPath) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos;
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

    /**
     * write xml file
     * @param content the content of the xml file
     * @param workingPath the working directory path
     * @param filename the name of the file
     * @return the path of the file
     * @throws IOException thrown an exception if the file is not writable
     */
    public static Path writeFile(StringBuilder content, Path workingPath, String filename) throws IOException {
        Path filePath = folderPathResolver(workingPath.toString() + "/" + filename);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath);
        bufferedWriter.append(content);
        bufferedWriter.close();
        return filePath;
    }

    /**
     * read a list of generic object type
     * @param filePath the path of the file
     * @param type the type of the object
     * @param <T> the generic type T
     * @return the list of T object
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readListObject(Path filePath, Class<T> type){

        ObjectInputStream in;
        FileInputStream fis = null;
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
        return (List<T>) o;
    }

    /**
     * read a generic object type
     * @param filePath the path of the file
     * @param type the type of the object
     * @param <T> the generic type T
     * @return the T object
     */
    public static <T>T readObject(Path filePath, Class<T> type){

        ObjectInputStream in;
        FileInputStream fis = null;
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
        return type.cast(o);
    }

    /**
     * normalize a path
     * @param path the path to normalize
     * @return the normalized path
     */
    public static Path folderPathResolver(String path){
        return Paths.get(path).normalize();
    }

    public static void createFolder(Path path) {
        File folder = path.toFile();
        try {
            if (Files.exists(path)){
                FileUtils.deleteDirectory(folder);
            }
            if (!folder.mkdir()){
                throw new IOException("cannot create output folder");
            }
        }
        catch (IOException e) {
            LOGGER.error("error during create folder method : ",e);
        }

    }
}
