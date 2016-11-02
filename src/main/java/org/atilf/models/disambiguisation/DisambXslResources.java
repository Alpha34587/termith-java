package org.atilf.models.disambiguisation;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslResources {
    public class XslResources {
        public final TransformerFactory FACTORY;
        private final Source DISAMB;

        public XslResources() {
            this.DISAMB = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsl/disamb.xsl"));
            this.FACTORY = TransformerFactory.newInstance();
        }
    }
}
