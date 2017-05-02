package org.atilf.models.disambiguation;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

/**
 * this class contains the namespace of xml declaration used during the dom parsing of tei working file format and
 * the xpath used by this parser
 * @author Simon Meoni
 *         Created on 18/10/16.
 */
public class DisambiguationExporterResource {

    private DisambiguationExporterResource() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * declaration of namespace _context :
     *  - ns & ns2 are namespaces for standoff annotation
     *  - tei is the namespace for tei format
     */
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

    /**
     * some xpath
     */
    public static final String TEI_SPAN;
    public static final String SPAN;
    public static final String TARGET;
    public static final String CORRESP;
    public static final String ANA;
    public static final String CONTEXT_GETTER_SIMPLE;
    public static final String TAG_GETTER;
    public static final String CONTEXT_GETTER_LAST;
    public static final String CONTEXT_GETTER_FIRST;

    static {
        /*
        used for extract span
         */
        TEI_SPAN = "//ns:standOff[@type = 'candidatsTermes']/ns:listAnnotation/tei:span";
        SPAN = "//ns:standOff/tei:span";
        /*
        used for extract target attribute of term candidate
         */
        TARGET = "@target";
        /*
        used for extract corresp attribute of term candidate
         */
        CORRESP = "@corresp";
        /*
        used for extract ana attribute of term candidate
         */
        ANA = "@ana";
        /*
        used for extract tei:w element
         */
        TAG_GETTER = ".//tei:w";
        /*
        used for extract _context
         */
        CONTEXT_GETTER_SIMPLE = "/tei:TEI/tei:text//tei:w[@xml:id = $c_id]/..";
        CONTEXT_GETTER_FIRST = "/tei:TEI/tei:text//tei:w[@xml:id = $first]/..";
        CONTEXT_GETTER_LAST = "/tei:TEI/tei:text//tei:w[@xml:id = $last]/..";
    }

}
