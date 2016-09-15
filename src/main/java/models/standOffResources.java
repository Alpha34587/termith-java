package models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class standOffResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(standOffResources.class.getName());
    private final static String PATH = "src/main/resources/standoff/";

    public final static String LIST_ANNOTATION = readFiles(PATH + "list-annotation.xml");
    public final static String MS_SPAN = readFiles(PATH + "ms-span.xml");
    public final static String T_SPAN = readFiles(PATH + "t-span.xml");
    public final static String MS_TEI_HEADER = readFiles(PATH + "ms-tei-header.xml");
    public final static String T_TEI_HEADER = readFiles(PATH + "t-tei-header.xml");
    public final static String T_INTERP_GRP = readFiles(PATH + "t-interp-grp.xml");
    public final static String STANDOFF = readFiles(PATH + "standoff.xml");


    private static String readFiles(String path)  {
        try {
            return String.join("\n",Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            LOGGER.error("cannot read resources",e);
        }
        return "";
    }
}
