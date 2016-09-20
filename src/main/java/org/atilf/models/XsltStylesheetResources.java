package org.atilf.models;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class XsltStylesheetResources {
    public static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    public static final Source EXTRACT_TEXT = new StreamSource(new File("src/main/resources/xsl/ExtractText.xsl"));
}
