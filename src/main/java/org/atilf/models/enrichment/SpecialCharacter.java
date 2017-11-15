package org.atilf.models.enrichment;

public class SpecialCharacter extends SpecialChXmlEscape{

    private SpecialCharacter() {
        super();
    }

    static {
        SPEC_CH.put("’", "'");
        SPEC_CH.put("ʼ", "'");
        SPEC_CH.put("՚", "'");
    }
}

