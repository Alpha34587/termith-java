package org.atilf.models;

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
        this._begin = begin;
        this._end = end;
        this.word = word;
        this.termId = id;
        this._ids = new ArrayList<>();
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
        this._ids.addAll(ids);
    }

    public TermsOffsetId(TermsOffsetId other) {
        this._begin = other._begin;
        this._end = other._end;
        this.word = other.word;
        this.termId = other.termId;
        this._ids = new ArrayList<>();
    }
}
