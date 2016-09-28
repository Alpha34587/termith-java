package org.atilf.models;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class StandOffResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandOffResources.class.getName());
    private final static String PATH = "standoff/";

    public final StringBuilder LIST_ANNOTATION = indentation(readFile(PATH + "list-annotation.xml"), 2);
    public final StringBuilder MS_SPAN = indentation(readFile(PATH + "ms-span.xml"), 3);
    public final StringBuilder T_SPAN = indentation(readFile(PATH + "t-span.xml"), 3);
    public final StringBuilder MS_TEI_HEADER = indentation(readFile(PATH + "ms-tei-header.xml"), 2);
    public final StringBuilder T_TEI_HEADER = indentation(readFile(PATH + "t-tei-header.xml"), 2);
    public final StringBuilder T_INTERP_GRP = indentation(readFile(PATH + "t-interp-grp.xml"), 2);
    public final StringBuilder STANDOFF = indentation(readFile(PATH + "standoff.xml"), 1);
    public final StringBuilder NS = indentation(readFile(PATH + "ns.txt"),0);



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

    private StringBuilder indentation(String input, int level) {
        String output = "";
        String ident = "";
        while (level != 0) {
            ident += "\t";
            level--;
        }

        for (String line : input.split("(?<=\n)")) {
            output += ident + line;
        }

        return new StringBuilder(output);
    }
}
