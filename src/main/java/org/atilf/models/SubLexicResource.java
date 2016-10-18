package org.atilf.models;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

/**
 * @author Simon Meoni
 *         Created on 18/10/16.
 */
public class SubLexicResource {
    public static final NamespaceContext NAMESPACE_CONTEXT = new NamespaceContext() {
        @Override
        public String getNamespaceURI(String s) {
            String uri;
            if (s.equals("ns") || s.equals("ns2"))
                uri = "http://standoff.proposal";
            else
                uri = "http://www.tei-c.org/ns/1.0";
            return uri;        }

        @Override
        public String getPrefix(String s) {
            return null;
        }

        @Override
        public Iterator getPrefixes(String s) {
            return null;
        }
    };
}
