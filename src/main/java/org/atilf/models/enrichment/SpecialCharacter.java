package org.atilf.models.enrichment;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SpecialCharacter extends SpecialChXmlEscape{
    private final static Map<ByteBuffer, String> SPEC_CH = new ConcurrentHashMap<>();
    private SpecialCharacter() {
        super();
    }

    static {
        SPEC_CH.put(ByteBuffer.wrap("ʼ".getBytes()), "\'");
        SPEC_CH.put(ByteBuffer.wrap("’".getBytes()), "\'");
        SPEC_CH.put(ByteBuffer.wrap("՚".getBytes()), "\'");
    }

    /**
     * replace a special character to a xml escape characters
     * @param s the character
     * @return the converted character
     */
    public static String replaceChar(String s){
        return Stream.of(s.split(""))
                .map(el -> SPEC_CH.getOrDefault(ByteBuffer.wrap(el.getBytes()), el))
                .reduce("", String::concat);
    }
}

