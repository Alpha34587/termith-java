package org.atilf.models.disambiguation;

import org.atilf.models.extractor.XslResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * the xsl resource used during the disambiguation. the xsl stylesheet converts tei file into working file format
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslResources extends XslResources{

        private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationXslResources.class.getName());
        public DisambiguationXslResources() {
            _stylesheet = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/disambiguation.xsl"));
            try {
                _factory = TransformerFactory.newInstance().newTemplates(_stylesheet);
            } catch (TransformerConfigurationException e) {
                LOGGER.error("cannot parse xsl stylesheet",e);
            }
        }
}
