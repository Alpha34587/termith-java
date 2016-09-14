package models;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyOffetId extends OffsetId {
    private final String word;

    protected TerminologyOffetId(int begin, int end, int id, String word) {
        super(begin, end, id);
        this.word = word;
    }
}
