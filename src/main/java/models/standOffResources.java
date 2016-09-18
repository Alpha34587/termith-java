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

    public final static String LIST_ANNOTATION = indentation(readFiles(PATH + "list-annotation.xml"), 2);
    public final static String MS_SPAN = indentation(readFiles(PATH + "ms-span.xml"), 3);
    public final static String T_SPAN = indentation(readFiles(PATH + "t-span.xml"), 3);
    public final static String MS_TEI_HEADER = indentation(readFiles(PATH + "ms-tei-header.xml"), 2);
    public final static String T_TEI_HEADER = indentation(readFiles(PATH + "t-tei-header.xml"), 2);
    public final static String T_INTERP_GRP = indentation(readFiles(PATH + "t-interp-grp.xml"), 2);
    public final static String STANDOFF = indentation(readFiles(PATH + "standoff.xml"), 1);
    public final static String NS = readFiles(PATH + "ns.txt");



    private static String readFiles(String path)  {
        try {
            return String.join("\n", Files.readAllLines(Paths.get(path))) + "\n";
        } catch (IOException e) {
            LOGGER.error("cannot read resources",e);
        }
        return "";
    }

    private static String indentation(String input, int level) {
        String output = "";
        String ident = "";
        while (level != 0) {
            ident += "\t";
            level--;
        }

        for (String line : input.split("(?<=\n)")) {
            output += ident + line;
        }

        return output;
    }
}
