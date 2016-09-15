package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class OffsetId {
    int begin;
    int end;
    List<Integer> ids;

    protected OffsetId(int begin, int end, int id) {
        this.begin = begin;
        this.end = end;
        this.ids = new ArrayList<>();
        ids.add(id);
    }

    public OffsetId() {

    }


    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public List<Integer> getIds() {
        return ids;
    }
}
