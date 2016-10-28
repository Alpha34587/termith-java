package org.atilf.models.extractor;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class XslResources {
    public final TransformerFactory FACTORY;
    public final Source EXTRACT_TEXT;

    public XslResources() {
        this.EXTRACT_TEXT = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/ExtractText.xsl"));
        this.FACTORY = TransformerFactory.newInstance();
    }
}
