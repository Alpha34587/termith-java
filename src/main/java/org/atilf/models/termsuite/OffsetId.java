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

    public void set_begin(int _begin) {
        this._begin = _begin;
    }

    public void set_end(int _end) {
        this._end = _end;
    }

    public int get_begin() {
        return _begin;
    }

    public int get_end() {
        return _end;
    }

    public List<Integer> get_ids() {
        return _ids;
    }
}
