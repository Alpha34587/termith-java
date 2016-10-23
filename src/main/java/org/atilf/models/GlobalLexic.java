package org.atilf.models;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.util.concurrent.ExecutionError;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Simon Meoni
 *         Created on 23/10/16.
 */
public class GlobalLexic implements Iterable<String>{
    private Multiset<String> globalLexic;
    private Map<Integer,String> lexicalEntry;
    private Map<String, Integer> idEntry;
    int counter = 0;

    public GlobalLexic(Map<Integer,String> lexicalEntry, Map<String,Integer> idEntry) {
        globalLexic = HashMultiset.create();
        this.idEntry = idEntry;
        this.lexicalEntry = lexicalEntry;
    }

    public Multiset getGlobalLexic() {
        return globalLexic;
    }

    public int size(){
        return globalLexic.size();
    }

    public Map<Integer, String> getLexicalEntry() {
        return lexicalEntry;
    }

    public void addEntry(String entry){
        globalLexic.add(entry);
        if (!idEntry.containsKey(entry)) {
            idEntry.put(entry, counter);
            lexicalEntry.put(counter, entry);
            counter++;
        }
    }

    public int getCount(String el){
        return globalLexic.count(el);
    }

    public String getIdEntry(String el){
        return idEntry.get(el).toString();
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super String> consumer) {
        globalLexic.elementSet().forEach(consumer);
    }
}
