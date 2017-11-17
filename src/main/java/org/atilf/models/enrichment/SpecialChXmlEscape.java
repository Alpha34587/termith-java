package org.atilf.models.enrichment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * this is an equivalence table used during the exportation to tei file
 * @author Simon Meoni
 *         Created on 29/09/16.
 */
public class SpecialChXmlEscape {
    protected final static Map<String, String> SPEC_CH = new ConcurrentHashMap<>();

    SpecialChXmlEscape() {
        throw new IllegalAccessError("Utility class");
    }

    static {
        SPEC_CH.put("\"", "&quot;");
        SPEC_CH.put("&", "&amp;");
        SPEC_CH.put("'", "&apos;");
        SPEC_CH.put("<", "&lt;");
        SPEC_CH.put(">", "&gt;");
    }

    /**
     * replace a special character to a xml escape characters
     * @param s the character
     * @return the converted character
     */
    public static String replaceChar(String s){
        return Stream.of(s.split(""))
                .map(c -> SPEC_CH.getOrDefault(c, c))
                .reduce("", String::concat);
    }
}
