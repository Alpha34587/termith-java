package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class LexicalProfile {
    Multiset lexicalTable;
    Map<String,Float> specCoefficientMap;

    public LexicalProfile(Multiset lexicalTable) {
        this.lexicalTable = lexicalTable;
        specCoefficientMap = new HashMap<>();
    }

    public Multiset getLexicalTable() {
        return lexicalTable;
    }

    public int countOccurence(String word){
        if (lexicalTable.contains(word)){
            return lexicalTable.count(word);
        }
        else
            return -1;
    }

    public void addCoefficientSpec(String entry, Float coefficient){
        if (lexicalTable.contains(entry)) {
            specCoefficientMap.put(entry,coefficient);
        }
        else{
            throw new NullPointerException("Multiset Object not contains this entry " + entry);
        }
    }
}
