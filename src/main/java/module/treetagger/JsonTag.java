package module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class JsonTag {

    int begin;
    int end;
    String lemma;
    String tag;

    public JsonTag(){}

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
