package org.atilf.models.extractor;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class XslResources {
    public TransformerFactory _factory;
    public Source _stylesheet;

    public XslResources() {
        _stylesheet = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/ExtractText.xsl"));
        _factory = TransformerFactory.newInstance();
    }
}
