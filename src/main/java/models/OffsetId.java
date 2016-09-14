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

    private OffsetId(int begin, int end, int id) {
        this.begin = begin;
        this.end = end;
        this.ids = new ArrayList<>();
        ids.add(id);
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

    public static void addId(List<OffsetId> offsetIds, int id){
        offsetIds.get(offsetIds.size()-1).ids.add(id);
    }

    public static void addNewOffset(List<OffsetId> offsetIds, int begin, int end, int id){
        offsetIds.add(new OffsetId(begin,end,id));
    }
}
