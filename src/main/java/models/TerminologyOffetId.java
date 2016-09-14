package models;

import java.util.ArrayList;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyOffetId extends OffsetId {
    private final String word;
    private final int termId;
    protected TerminologyOffetId(int begin, int end, int id, String word) {
        super();
        this.begin = begin;
        this.end = end;
        this.word = word;
        this.termId = id;
        this.ids = new ArrayList<>();

    }

    public String getWord() {
        return word;
    }

    public int getTermId() {
        return termId;
    }
}
