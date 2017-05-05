package org.atilf.models.enrichment;

import java.util.List;

/**
 * the MorphologyOffsetId is a object who contains the lemma, tag, begin, end and id of a word.
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class MorphologyOffsetId extends OffsetId{

    private String _lemma;
    private String _tag;

    /**
     * constructor of MorphologyOffsetId
     * @param begin the beginning offset value of a word
     * @param end the ending offset value of a word
     * @param lemma the lemma of a word
     * @param tag the POS of a word
     * @param id the xml id of a word
     */
    public MorphologyOffsetId(int begin, int end, String lemma, String tag , int id) {
        super(begin, end, id);
        _lemma = lemma;
        _tag = tag;
    }

    /**
     * constructor of MorphologyOffsetId
     * @param begin the beginning offset value of a word
     * @param end the ending offset value of a word
     * @param lemma the lemma of a word
     * @param tag the POS of a word
     * @param ids the xml ids of a word
     */
    public MorphologyOffsetId(int begin, int end, String lemma, String tag, List<Integer> ids) {
        super(begin,end,ids);
        _lemma = lemma;
        _tag = tag;
    }

    /**
     * get the lemma of a word
     * @return return the lemma
     */
    public String getLemma() {
        return _lemma;
    }

    /**
     * return the tag
     * @return return the POS tag
     */
    public String getTag() {
        return _tag;
    }

    /**
     * add an id of the last word in the list of MorphologyOffsetId
     * @param offsetIds the list of MorphologyOffsetId
     * @param id the id
     */
    public static void addId(List<MorphologyOffsetId> offsetIds, int id){
        offsetIds.get(offsetIds.size()-1).getIds().add(id);
    }

    /**
     * add new word in the list of MorphologyOffsetId
     * @param offsetIds the list of MorphologyOffsetId
     * @param begin the beginning offset value of a word
     * @param end the ending offset value of a word
     * @param lemma the lemma of a word
     * @param tag the POS of a word
     * @param id the xml id of a word
     */
    public static void addNewOffset(List<MorphologyOffsetId> offsetIds, int begin, int end, String lemma, String tag,
                                    int id){
        offsetIds.add(new MorphologyOffsetId(begin,end, lemma, tag, id));
    }
}
