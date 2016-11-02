package org.atilf.models.disambiguisation;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslResources {

        final TransformerFactory _factory;
        final Source _disamb;

        public DisambXslResources() {
            _disamb = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/disamb.xsl"));
            _factory = TransformerFactory.newInstance();
        }

        public TransformerFactory getFactory() {
            return _factory;
        }

        public Source getDisamb() {
            return _disamb;
        }
}
