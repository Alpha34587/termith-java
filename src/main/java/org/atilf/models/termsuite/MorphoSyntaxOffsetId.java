package org.atilf.models.termsuite;

import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class MorphoSyntaxOffsetId extends OffsetId{

    String _lemma;
    String _tag;
    public MorphoSyntaxOffsetId(int begin,int end,String lemma, String tag ,int id) {
        super(begin, end, id);
        _lemma = lemma;
        _tag = tag;
    }

    public MorphoSyntaxOffsetId(int begin, int end, String lemma, String tag, List<Integer> ids) {
        super(begin,end,ids);
        _lemma = lemma;
        _tag = tag;
    }

    public String get_lemma() {
        return _lemma;
    }

    public String get_tag() {
        return _tag;
    }

    public static void addId(List<MorphoSyntaxOffsetId> offsetIds, int id){
        offsetIds.get(offsetIds.size()-1)._ids.add(id);
    }

    public static void addNewOffset(List<MorphoSyntaxOffsetId> offsetIds, int begin, int end, String lemma, String tag,
                                    int id){
        offsetIds.add(new MorphoSyntaxOffsetId(begin,end, lemma, tag, id));
    }
}
