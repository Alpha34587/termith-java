package org.atilf.models.termsuite;

import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TermOffsetId extends OffsetId {
    private String _word;
    private int _termId;
    public TermOffsetId(int begin, int end, int id, String word) {
        super();
        _begin = begin;
        _end = end;
        _word = word;
        _termId = id;
    }

    public TermOffsetId() {}

    public void setWord(String _word) {
        this._word = _word;
    }

    public void setTermId(int _termId) {
        this._termId = _termId;
    }

    public String getWord() {
        return _word;
    }

    public int getTermId() {
        return _termId;
    }

    public void setIds(List<Integer> ids){
        this._ids.addAll(ids);
    }

    public TermOffsetId(TermOffsetId other) {
        _begin = other._begin;
        _end = other._end;
        _word = other._word;
        _termId = other._termId;
    }
}
