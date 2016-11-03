package org.atilf.models.termsuite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class OffsetId implements Serializable {
    int _begin;
    int _end;
    List<Integer> _ids = new ArrayList<>();

    protected OffsetId(int begin, int end, int id) {
        _begin = begin;
        _end = end;
        _ids.add(id);
    }

    protected OffsetId(int begin, int end, List<Integer> ids) {
        this._begin = begin;
        this._end = end;
        this._ids = ids;
    }

    public OffsetId() {}

    public void setBegin(int _begin) {
        this._begin = _begin;
    }

    public void setEnd(int _end) {
        this._end = _end;
    }

    public int getBegin() {
        return _begin;
    }

    public int getEnd() {
        return _end;
    }

    public List<Integer> getIds() {
        return _ids;
    }
}
