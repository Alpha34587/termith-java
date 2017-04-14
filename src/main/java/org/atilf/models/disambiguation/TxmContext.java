package org.atilf.models.disambiguation;

import java.util.Stack;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmContext extends LexiconProfile{
    Stack<String> _lemma = new Stack<>();
    Stack<String> _pos = new Stack<>();
    Stack<String> _word = new Stack<>();
    Stack<String> _target = new Stack<>();
    String _filename = "";
    public void addElements(String target,String lemma, String pos, String word){
        _lemma.add(lemma);
        _pos.add(pos);
        _word.add(word);
        _target.add(target);
    }

    public String getLemma() {
        return _lemma.pop();
    }

    public String getPos() {
        return _pos.pop();
    }

    public String getWord() {
        return _word.pop();
    }

    public String getTarget() {
        return _target.pop();
    }

    public String getFilename() {
        return _filename;
    }

    public boolean isEmpty(){
        return _lemma.isEmpty();
    }

    public void setFilename(String filename) {
        this._filename = filename;
    }
}
