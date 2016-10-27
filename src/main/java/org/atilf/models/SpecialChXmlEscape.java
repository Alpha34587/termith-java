package org.atilf.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.atilf.models.TagNormalizer.FR_TT_TAG;

/**
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

    public static String replaceXmlChar(String occ){
        for (Map.Entry <String,String> entry : XML_SPEC_CH.entrySet()){
            occ = occ.replace(entry.getKey(),entry.getValue());
        }
        return occ;
    }
}
