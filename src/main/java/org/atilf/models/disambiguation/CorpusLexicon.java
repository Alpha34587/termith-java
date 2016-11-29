package org.atilf.models.disambiguation;

import java.util.Map;

/**
 * the corpus lexicon is an object who contains the number of occurrences for each words of a corpus
 *         Created on 23/10/16.
 */
public class CorpusLexicon extends Lexicon{
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
        super();
        _idEntry = idEntry;
        _lexicalEntry = lexicalEntry;
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
    public synchronized void addEntry(String entry){
        _lexicalTable.add(entry);
        if (!_idEntry.containsKey(entry)) {
            _idEntry.put(entry, _counter);
            _lexicalEntry.put(_counter, entry);
            _counter++;
        }
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
}
