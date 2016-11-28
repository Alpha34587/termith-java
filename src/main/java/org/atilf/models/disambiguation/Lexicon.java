package org.atilf.models.disambiguation;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Simon Meoni Created on 28/11/16.
 */
public abstract class Lexicon implements Iterable<String> {
    
    Multiset<String> _lexicalTable;
    /**
     * a constructor for evaluationProfile
     * @param lexicalTable an already initialized multiset
     */
    Lexicon(Multiset<String> lexicalTable) {
        _lexicalTable = lexicalTable;
    }

    /**
     * empty constructor of this class
     */
    Lexicon() { _lexicalTable = ConcurrentHashMultiset.create(); }

    /**
     * get _lexicalTable of this class
     * @return a multiset
     */
    public Multiset<String> getLexicalTable() {
        return _lexicalTable;
    }

    /**
     * get the size of _lexicalTable
     * @return return the size of the lexicalTable
     */
    int lexicalSize(){
        return _lexicalTable.size();
    }

    /**
     * add a word to _lexicalTable
     * @param word the word
     */
    public void addOccurrence(String word){
        _lexicalTable.add(word);
    }

    /**
     * count the number of occurrences of a word
     * @param word a word to count in the corpus
     * @return return the number of occurrence of word
     */
    public int count(String word){
        if (_lexicalTable.contains(word)){
            return _lexicalTable.count(word);
        }
        else
            return -1;
    }

    /**
     * override iterator
     * @return return String iterator
     */
    @Override
    public Iterator<String> iterator() {
        return null;
    }

    /**
     * this foreach lambda method is associated to the multiset of the corpus
     * @param consumer the current string consumer
     */
    @Override
    public void forEach(Consumer<? super String> consumer) {
        _lexicalTable.elementSet().forEach(consumer);
    }

}
