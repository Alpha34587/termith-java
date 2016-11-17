package org.atilf.models.disambiguation;

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
    public static final String TEI_SPAN;
    public static final String SPAN;
    public static final String TARGET;
    public static final String CORRESP;
    public static final String ANA;
    public static final String CONTEXT_GETTER_SIMPLE;
    public static final String TAG_GETTER;
    public static final String CONTEXT_GETTER_LAST;
    public static final String CONTEXT_GETTER_FIRST;

    private final static Logger LOGGER = LoggerFactory.getLogger(ContextResources.class);
    static {
        TEI_SPAN = "//ns:standOff[@type = 'candidatsTermes']/ns:listAnnotation/tei:span";
        SPAN = "//ns:standOff/tei:span";
        TARGET = "@target";
        CORRESP = "@corresp";
        ANA = "@ana";
        TAG_GETTER = ".//tei:w";
        CONTEXT_GETTER_SIMPLE = "/tei:TEI/tei:text//tei:w[@xml:id = $c_id]/..";
        CONTEXT_GETTER_FIRST = "/tei:TEI/tei:text//tei:w[@xml:id = $first]/..";
        CONTEXT_GETTER_LAST = "/tei:TEI/tei:text//tei:w[@xml:id = $last]/..";
    }

}
