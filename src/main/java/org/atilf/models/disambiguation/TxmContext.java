package org.atilf.models.disambiguation;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmContext extends LexiconProfile{
    private Stack<String> _lemma = new Stack<>();
    private Stack<String> _pos = new Stack<>();
    private Stack<String> _word = new Stack<>();
    private Stack<String> _target = new Stack<>();
    private String _filename = "";
    public void addElements(String target,String lemma, String pos, String word){
        _lemma.add(lemma);
        _pos.add(pos);
        _word.add(word);
        _target.add(target);
    }

    public Map<String,String> getTxmWord() {
        Map<String, String> resMap = new HashMap<>();
        if (!_lemma.isEmpty()) {
            resMap.put("lemma", _lemma.pop());
            resMap.put("pos", _pos.pop());
            resMap.put("word", _word.pop());
            resMap.put("target", _target.pop());
        }
        return resMap;
    }

    public boolean isEmpty(){
        return _lemma.isEmpty();
    }

    public void setFilename(String filename) {
        this._filename = filename;
    }

    public String getFilename() {
        return _filename;
    }
}
