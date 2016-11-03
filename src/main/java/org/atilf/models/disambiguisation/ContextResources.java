package org.atilf.models.disambiguisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

/**
 * @author Simon Meoni
 *         Created on 18/10/16.
 */
public class ContextResources {
    public static final NamespaceContext NAMESPACE_CONTEXT = new NamespaceContext() {
        @Override
        public String getNamespaceURI(String s) {
            String uri;
            switch (s) {
                case "ns":
                case "ns2":
                    uri = "http://standoff.proposal";
                    break;
                case "xml":
                    uri = XMLConstants.XML_NS_URI;
                    break;
                default:
                    uri = "http://www.tei-c.org/ns/1.0";
                    break;
            }
            return uri;
        }

        @Override
        public String getPrefix(String s) {
            return null;
        }

        @Override
        public Iterator getPrefixes(String s) {
            return null;
        }
    };
    public static final String SPAN_T;
    public static final String TARGET_T;
    public static final String CORRESP_T;
    public static final String ANA_T;
    public static final String MULTI_TEXT;
    public static final String SIMPLE_TEXT;
    private final static Logger LOGGER = LoggerFactory.getLogger(ContextResources.class);

    static {
        SPAN_T = "//ns:standOff/tei:span";
        TARGET_T = "@target";
        CORRESP_T = "@corresp";
        ANA_T = "@ana";
        SIMPLE_TEXT = "/tei:TEI/tei:text//tei:w[./preceding-sibling::tei:w[@xml:id = $c_id ] " +
                "or ./following-sibling::tei:w[@xml:id = $c_id]]";
        MULTI_TEXT = "/tei:TEI/tei:text//tei:w[./preceding-sibling::tei:w[@xml:id = $last] or " +
                "./following-sibling::tei:w[@xml:id = $first]]";
    }

}
