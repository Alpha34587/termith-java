package org.atilf.models;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 23/10/16.
 */
public class GlobalLexic {
    private Multiset globalLexic;
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
        if (!lexicalEntry.containsKey(entry)) {
            idEntry.put(entry, counter);
            lexicalEntry.put(counter, entry);
            counter++;
        }
    }

}
