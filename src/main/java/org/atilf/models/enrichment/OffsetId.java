package org.atilf.models.enrichment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * the abstract class used to represents the uima annotation of termsuite
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public abstract class OffsetId implements Serializable {
    int _begin;
    int _end;
    List<Integer> _ids = new ArrayList<>();

    /**
     * constructor of OffsetId
     * @param begin the beginning offset value
     * @param end the ending offset value
     * @param id the xml id of an annotation
     */
    protected OffsetId(int begin, int end, int id) {
        _begin = begin;
        _end = end;
        _ids.add(id);
    }


    /**
     * constructor of OffsetId
     * @param begin the beginning offset value
     * @param end the ending offset value
     * @param ids the xml ids of an annotation
     */
    protected OffsetId(int begin, int end, List<Integer> ids) {
        _begin = begin;
        _end = end;
        _ids = ids;
    }

    /**
     * the empty constructor of OffsetId
     */
    OffsetId() {}

    /**
     * setter of _begin
     * @param begin an integer
     */
    public void setBegin(int begin) {
        _begin = begin;
    }

    /**
     * setter of _end
     * @param _end an integer
     */
    public void setEnd(int _end) {
        this._end = _end;
    }

    /**
     * getter of _begin
     * @return the beginning offset value
     */
    public int getBegin() {
        return _begin;
    }

    /**
     * getter of _end
     * @return the ending offset value
     */
    public int getEnd() {
        return _end;
    }

    /**
     * get the list ids of an annotation
     * @return a list who contains the set of id of an annotation
     */
    public List<Integer> getIds() {
        return _ids;
    }
}
