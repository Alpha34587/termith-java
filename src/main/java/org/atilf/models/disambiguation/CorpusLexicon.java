package org.atilf.models.disambiguation;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Simon Meoni
 *         Created on 23/10/16.
 */
public class CorpusLexicon implements Iterable<String>{
    private Multiset<String> _multisetLexicon = ConcurrentHashMultiset.create();
    private Map<Integer,String> _lexicalEntry;
    private Map<String, Integer> _idEntry;
    private int _counter = 0;

    public CorpusLexicon(Map<Integer,String> lexicalEntry, Map<String,Integer> idEntry) {
        _idEntry = idEntry;
        _lexicalEntry = lexicalEntry;
    }

    public Multiset getMultisetLexicon() {
        return _multisetLexicon;
    }

    public int size(){
        return _multisetLexicon.size();
    }

    public Map<Integer, String> getLexicalEntry() {
        return _lexicalEntry;
    }

    public void addEntry(String entry){
        _multisetLexicon.add(entry);
        if (!_idEntry.containsKey(entry)) {
            _idEntry.put(entry, _counter);
            _lexicalEntry.put(_counter, entry);
            _counter++;
        }
    }

    public int count(String el){
        return _multisetLexicon.count(el);
    }

    public String getIdEntry(String el){
        return _idEntry.get(el).toString();
    }

    public String getLexicalEntry(int el){
        return _lexicalEntry.get(el);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super String> consumer) {
        _multisetLexicon.elementSet().forEach(consumer);
    }
}
