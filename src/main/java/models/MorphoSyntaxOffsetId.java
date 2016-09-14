package models;

import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class MorphoSyntaxOffsetId extends OffsetId{

    String lemma;
    String tag;
    protected MorphoSyntaxOffsetId(int begin,int end,String lemma, String tag ,int id) {
        super(begin, end, id);
        this.lemma = lemma;
        this.tag = tag;
    }

    public String getLemma() {
        return lemma;
    }

    public String getTag() {
        return tag;
    }

    public static void addId(List<MorphoSyntaxOffsetId> offsetIds, int id){
        offsetIds.get(offsetIds.size()-1).ids.add(id);
    }

    public static void addNewOffset(List<MorphoSyntaxOffsetId> offsetIds, int begin, int end, String lemma, String tag,
                                    int id){
        offsetIds.add(new MorphoSyntaxOffsetId(begin,end, lemma, tag, id));
    }
}
