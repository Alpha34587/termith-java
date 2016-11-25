package org.atilf.models.disambiguation;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * the corpus lexicon is an object who contains the number of occurrences for each words of a corpus
 *         Created on 23/10/16.
 */
public class CorpusLexicon implements Iterable<String>{
    private Multiset<String> _multisetLexicon = ConcurrentHashMultiset.create();
    private Map<Integer,String> _lexicalEntry;
    private Map<String, Integer> _idEntry;
    private int _counter = 0;

    /**
     * constructor of CorpusLexicon, each word is associated to an id. these objects is used to simplify the
     * R treatment.
     * @param lexicalEntry the map who links an id to a word
     * @param idEntry the map who links a word to an id
     */
    public CorpusLexicon(Map<Integer,String> lexicalEntry, Map<String,Integer> idEntry) {
        _idEntry = idEntry;
        _lexicalEntry = lexicalEntry;
    }

    /**
     * getter of the multiset of the entire corpus
     * @return return a multiset variable
     */
    public Multiset getMultisetLexicon() {
        return _multisetLexicon;
    }

    /**
     * the size of the corpus
     * @return return the size of the corpus
     */
    public int size(){
        return _multisetLexicon.size();
    }

    /**
     * get the map with id associated to words
     * @return return the map with id associated to words
     */
    public Map<Integer, String> getLexicalEntry() {
        return _lexicalEntry;
    }

    /**
     * add a word to the corpus if it is a new word, the words is add to the two maps with ids and the counter
     * of the id is incremented
     * @param entry the word to add
     */
    public void addEntry(String entry){
        _multisetLexicon.add(entry);
        if (!_idEntry.containsKey(entry)) {
            _idEntry.put(entry, _counter);
            _lexicalEntry.put(_counter, entry);
            _counter++;
        }
    }

    /**
     * count the number of occurrences of a word
     * @param word a word to count in the corpus
     * @return return the number of occurrence of word
     */
    public int count(String word){
        return _multisetLexicon.count(word);
    }

    /**
     * @param word a word in the hashmap
     * @return return the id associated to a word
     */
    public String getIdEntry(String word){
        return _idEntry.get(word).toString();
    }

    /**
     * @param id an id in the hashmap
     * @return return the word associated to an id
     */
    public String getLexicalEntry(int id){
        return _lexicalEntry.get(id);
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
        _multisetLexicon.elementSet().forEach(consumer);
    }
}
