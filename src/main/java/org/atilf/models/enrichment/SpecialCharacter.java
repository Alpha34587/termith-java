package org.atilf.models.enrichment;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
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
     * @param ch the character
     * @return the converted character
     */
    public static String replaceChar(String ch){
        String[] splitCh = ch.split("");
        List<String> stringStream = Stream.of(splitCh).map(el -> {
            if (SPEC_CH.containsKey(ByteBuffer.wrap(el.getBytes())))
                return SPEC_CH.get(ByteBuffer.wrap(el.getBytes()));
            else
                return el;
        }).collect(Collectors.toList());

        return String.join("",stringStream);
    }
}

