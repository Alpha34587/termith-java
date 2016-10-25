package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationProfile implements Iterable<String> {
    Multiset<String> lexicalTable;
    String disambIdMap;

    public EvaluationProfile(Multiset lexicalTable) {
        this.lexicalTable = lexicalTable;
        disambIdMap = "noDa";
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

    public String getDisambIdMap() {
        return disambIdMap;
    }

    public void setDisambIdMap(String disambIdMap) {
        this.disambIdMap = disambIdMap;
    }


    void addOccurence(String occ){
        lexicalTable.add(occ);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super String> consumer) {
        lexicalTable.elementSet().forEach(consumer);
    }
}
