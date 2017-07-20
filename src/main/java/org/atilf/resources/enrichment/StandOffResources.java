package org.atilf.resources.enrichment;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * this class contains the xml fragment of used to write standoff _annotation
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class StandOffResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandOffResources.class.getName());
    private final static String PATH = "models/enrichment/standoffResources/";

    public static StringBuilder LIST_ANNOTATION;
    public static StringBuilder MS_SPAN;
    public static StringBuilder T_SPAN;
    public static StringBuilder PH_SPAN;
    public static StringBuilder LST_SPAN;
    public static StringBuilder MS_TEI_HEADER;
    public static StringBuilder T_TEI_HEADER;
    public static StringBuilder PH_TEI_HEADER;
    public static StringBuilder LST_TEI_HEADER;
    public static StringBuilder T_INTERP_GRP;
    public static StringBuilder STANDOFF;
    public static StringBuilder NS;


    public static void init(String resourcePath){
         LIST_ANNOTATION = indentation(readFile(PATH + "list-annotation.xml"), 2);
         MS_SPAN = indentation(readFile(PATH + "ms-span.xml"), 3);
         T_SPAN = indentation(readFile(PATH + "t-span.xml"), 3);
         PH_SPAN = indentation(readFile(PATH + "ph-span.xml"), 3);
         LST_SPAN = indentation(readFile(PATH + "lst-span.xml"), 3);
         MS_TEI_HEADER = indentation(readFile(PATH + "ms-tei-header.xml"), 2);
         T_TEI_HEADER = indentation(readFile(PATH + "t-tei-header.xml"), 2);
         PH_TEI_HEADER = indentation(readFile(PATH + "ph-tei-header.xml"), 2);
         LST_TEI_HEADER = indentation(readFile(PATH + "lst-tei-header.xml"), 2);
         T_INTERP_GRP = indentation(readFile(PATH + "t-interp-grp.xml"), 2);
         STANDOFF = indentation(readFile(PATH + "standoff.xml"), 1);
         NS = indentation(readFile(PATH + "ns.txt"),0);
    }
    /**
     * read a fragment
     * @param path path of a fragment
     * @return the fragment
     */
    
    private static String readFile(String path)  {
        try {
            InputStream resourceAsStream = StandOffResources.class.getClassLoader().getResourceAsStream(path);
            String xml = IOUtils.toString(resourceAsStream, "UTF-8");

            return xml + "\n";
        } catch (IOException e) {
            LOGGER.error("cannot read resources",e);
        }
        return "";
    }

    /**
     * indent a fragment
     * @param input a fragment
     * @param level the level of indentation
     * @return the transformed fragment
     */
    private static StringBuilder indentation(String input, int level) {
        StringBuilder output = new StringBuilder();
        StringBuilder indentation = new StringBuilder();
        while (level != 0) {
            indentation.append("\t");
            level--;
        }

        for (String line : input.split("(?<=\n)")) {
            output.append(indentation).append(line);
        }

        return output;
    }
}
