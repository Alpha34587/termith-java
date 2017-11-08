package org.atilf.models.disambiguation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simon Meoni on 14/04/17.
*/
public class TxmContext extends LexiconProfile{
    private Deque<String> _lemma = new ArrayDeque<>();
    private Deque<String> _pos = new ArrayDeque<>();
    private Deque<String> _word = new ArrayDeque<>();
    private Deque<String> _target = new ArrayDeque<>();
    private String _filename = "";
    public void addElements(String target,String lemma, String pos, String word){
        _lemma.add(lemma);
        _pos.add(pos);
        _word.add(word);
        _target.add(target);
    }

    public Map<String,String> popTxmWord() {
        Map<String, String> resMap = new HashMap<>();
        if (!_lemma.isEmpty()) {
            resMap.put("lemma", _lemma.pop());
            resMap.put("pos", _pos.pop());
            resMap.put("word", _word.pop());
            resMap.put("target", _target.pop());
        }
        return resMap;
    }

    public Map<String,String> getTxmWord() {
        Map<String, String> resMap = new HashMap<>();
        if (!_lemma.isEmpty()) {
            resMap.put("lemma", _lemma.peek());
            resMap.put("pos", _pos.peek());
            resMap.put("word", _word.peek());
            resMap.put("target", _target.peek());
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
