package org.atilf.models.enrichment;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * this class contains the xml fragment of used to write standoff annotation
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class StandOffResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandOffResources.class.getName());
    private final static String PATH = "models/enrichment/standoffResources/";

    public final StringBuilder LIST_ANNOTATION = indentation(readFile(PATH + "list-annotation.xml"), 2);
    public final StringBuilder MS_SPAN = indentation(readFile(PATH + "ms-span.xml"), 3);
    public final StringBuilder T_SPAN = indentation(readFile(PATH + "t-span.xml"), 3);
    public final StringBuilder MS_TEI_HEADER = indentation(readFile(PATH + "ms-tei-header.xml"), 2);
    public final StringBuilder T_TEI_HEADER = indentation(readFile(PATH + "t-tei-header.xml"), 2);
    public final StringBuilder T_INTERP_GRP = indentation(readFile(PATH + "t-interp-grp.xml"), 2);
    public final StringBuilder STANDOFF = indentation(readFile(PATH + "standoff.xml"), 1);
    public final StringBuilder NS = indentation(readFile(PATH + "ns.txt"),0);


    /**
     * read a fragment
     * @param path path of a fragment
     * @return the fragment
     */

    private String readFile(String path)  {
        try {
            InputStream resourceAsStream = getClass()
                    .getClassLoader().getResourceAsStream(path);
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
    private StringBuilder indentation(String input, int level) {
        String output = "";
        String identation = "";
        while (level != 0) {
            identation += "\t";
            level--;
        }

        for (String line : input.split("(?<=\n)")) {
            output += identation + line;
        }

        return new StringBuilder(output);
    }
}
