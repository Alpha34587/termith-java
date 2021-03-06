package org.atilf.resources.enrichment;

import org.atilf.module.enrichment.initializer.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * This is xsl resource for TextExtractor module. This class contains the xslStyleSheet to
 * extract plain text of a tei file.
 * @see TextExtractor
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class XslResources {
    public Templates _factory;
    public Source _stylesheet;
    private static final Logger LOGGER = LoggerFactory.getLogger(XslResources.class.getName());

    public XslResources(String resourcePath) {
        _stylesheet = new StreamSource(getClass().getClassLoader()
                .getResourceAsStream(resourcePath));
        try {
            _factory = TransformerFactory.newInstance().newTemplates(_stylesheet);
        } catch (TransformerConfigurationException e) {
            LOGGER.error("cannot parse xsl stylesheet",e);
        }
    }
}
