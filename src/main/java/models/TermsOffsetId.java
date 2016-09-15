package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TermsOffsetId extends OffsetId {
    private String word;
    private int termId;
    public TermsOffsetId(int begin, int end, int id, String word) {
        super();
        this.begin = begin;
        this.end = end;
        this.word = word;
        this.termId = id;
        this.ids = new ArrayList<>();
    }

    public TermsOffsetId() {
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }


    public String getWord() {
        return word;
    }

    public int getTermId() {
        return termId;
    }

    public void setIds(List<Integer> ids){
        this.ids.addAll(ids);
    }

    public TermsOffsetId(TermsOffsetId other) {
        this.begin = other.begin;
        this.end = other.end;
        this.word = other.word;
        this.termId = other.termId;
        this.ids = new ArrayList<>();
    }
}
