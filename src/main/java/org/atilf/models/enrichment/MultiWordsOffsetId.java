package org.atilf.models.enrichment;

import java.util.List;

/**
 * the MorphologyOffsetId is a object who contains the begin, end and id of a term occurrence.
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class MultiWordsOffsetId extends OffsetId {
    private String _word;
    private int _termId;

    /**
     * constructor of MorphologyOffsetId
     * @param begin the beginning offset value of a term occurrence
     * @param end the ending offset value of a term occurrence
     * @param word word of a term occurrence
     * @param id the xml id of a term occurrence
     */
    public MultiWordsOffsetId(int begin, int end, int id, String word) {
        super();
        _begin = begin;
        _end = end;
        _word = word;
        _termId = id;
    }


    /**
     * clone a termOffsetId
     * @param other a termOffsetId
     */
    public MultiWordsOffsetId(MultiWordsOffsetId other) {
        _begin = other._begin;
        _end = other._end;
        _word = other._word;
        _termId = other._termId;
    }

    /**
     * empty constructor of MultiWordsOffsetId
     */
    public MultiWordsOffsetId(){}

    public MultiWordsOffsetId(int begin, int end, Integer id, String word, List<Integer> ids) {
        this(begin, end, id, word);
        _ids = ids;
    }

    /**
     * setter for _word field
     * @param word a string of a word
     */
    public void setWord(String word) {
        _word = word;
    }

    /**
     * @return get the word
     */
    public String getWord() {
        return _word;
    }

    /**
     * @return get the termId
     */
    public int getTermId() {
        return _termId;
    }

    /**
     * set the list ids
     * @param ids the list of xml id
     */
    public void setIds(List<Integer> ids){
        getIds().addAll(ids);
    }

    /**
     * set the id of a term
     * @param termId the termId
     */
    public void setTermId(int termId) {
        _termId = termId;
    }

}
