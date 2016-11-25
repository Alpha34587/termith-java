package org.atilf.models.tei.exporter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this is an equivalence table used during the exportation to tei file
 * @author Simon Meoni
 *         Created on 29/09/16.
 */
public class SpecialChXmlEscape {
    protected final static Map<String, String> XML_SPEC_CH = new ConcurrentHashMap<>();

    static {
        XML_SPEC_CH.put("\"", "&quot;");
        XML_SPEC_CH.put("&", "&amp;");
        XML_SPEC_CH.put("'", "&apos;");
        XML_SPEC_CH.put("<", "&lt;");
        XML_SPEC_CH.put(">", "&gt;");
    }

    /**
     * replace a special character to a xml escape characters
     * @param ch the character
     * @return the converted character
     */
    public static String replaceXmlChar(String ch){
        for (Map.Entry <String,String> entry : XML_SPEC_CH.entrySet()){
            ch = ch.replace(entry.getKey(),entry.getValue());
        }
        return ch;
    }
}
