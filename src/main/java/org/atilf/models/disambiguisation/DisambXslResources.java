package org.atilf.models.disambiguisation;

import org.atilf.models.extractor.XslResources;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslResources extends XslResources{

        public DisambXslResources() {
            _stylesheet = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/disamb.xsl"));
            _factory = TransformerFactory.newInstance();
        }

        public TransformerFactory getFactory() {
            return _factory;
        }

        public Source getDisambStyleSheet() {
            return _stylesheet;
        }
}
