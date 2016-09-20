package org.atilf.models;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static eu.project.ttc.utils.TermSuiteConstants.R;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class XsltStylesheetResources {
    public static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    public static Source EXTRACT_TEXT;

    static {
        EXTRACT_TEXT = null;

        try {
            EXTRACT_TEXT = new StreamSource(new File(ResourceLoader.class.getClassLoader().getResource("./xsl/ExtractText.xsl").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
