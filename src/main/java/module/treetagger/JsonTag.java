package module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class JsonTag {

    private int begin;
    private int end;

    private String lemma;

    private String tag;

    public JsonTag(){}

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public String getLemma() {
        return lemma;
    }

    public String getTag() {
        return tag;
    }

    public void setOffset(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
