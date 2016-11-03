package org.atilf.models.extractor;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class XslResources {
    public final TransformerFactory _factory;
    public final Source _extractText;

    public XslResources() {
        _extractText = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/ExtractText.xsl"));
        _factory = TransformerFactory.newInstance();
    }
}
