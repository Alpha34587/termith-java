package org.atilf.models.enrichment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        SPEC_CH.put("<unknown>", "@unknown");
    }

    /**
     * replace a special character to a xml escape characters
     * @param ch the character
     * @return the converted character
     */
    public static String replaceChar(String ch){
        String res = "";
        for (Map.Entry <String,String> entry : SPEC_CH.entrySet()){
            res = ch.replace(entry.getKey(),entry.getValue());
        }
        return res;
    }
}
