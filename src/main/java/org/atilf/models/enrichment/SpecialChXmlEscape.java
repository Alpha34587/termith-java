package org.atilf.models.enrichment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this is an equivalence table used during the exportation to tei file
 * @author Simon Meoni
 *         Created on 29/09/16.
 */
public class SpecialChXmlEscape {
    protected final static Map<String, String> XML_SPEC_CH = new ConcurrentHashMap<>();

    private SpecialChXmlEscape() {
        throw new IllegalAccessError("Utility class");
    }

    static {
        XML_SPEC_CH.put("\"", "&quot;");
        XML_SPEC_CH.put("&", "&amp;");
        XML_SPEC_CH.put("'", "&apos;");
        XML_SPEC_CH.put("<", "&lt;");
        XML_SPEC_CH.put(">", "&gt;");
        XML_SPEC_CH.put("<unknown>", "@unknown");
    }

    /**
     * replace a special character to a xml escape characters
     * @param ch the character
     * @return the converted character
     */
    public static String replaceXmlChar(String ch){
        String res = "";
        for (Map.Entry <String,String> entry : XML_SPEC_CH.entrySet()){
            res = ch.replace(entry.getKey(),entry.getValue());
        }
        return res;
    }
}
