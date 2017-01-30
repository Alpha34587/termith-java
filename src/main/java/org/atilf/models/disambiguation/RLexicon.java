package org.atilf.models.disambiguation;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is the converted contextLexicon or the CorpusLexicon into R format
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexicon {
    private int _size;
    private StringBuffer _rName = new StringBuffer();
    private StringBuffer _rOcc = new StringBuffer();
    private CorpusLexicon _corpus;
    private LexiconProfile _lexiconProfile;
    private List<String> _idContextLexicon;

    /**
     * constructor used to convert corpus into R variable
     * @param corpus
     */
    public RLexicon(CorpusLexicon corpus){
        _corpus = corpus;
        _size = corpus.lexicalSize();
        _rName.append("c(");
        _rOcc.append("c(");
        _corpus.forEach(this::convertToRGlobal);
        closeRVariable();
    }

    /**
     * constructor used to convert a lexicon profile into R variable
     * @param lexiconProfile the lexicon profile
     * @param corpus the corpus
     */
    public RLexicon(LexiconProfile lexiconProfile, CorpusLexicon corpus) {
        _lexiconProfile = lexiconProfile;
        _corpus = corpus;
        _idContextLexicon = new ArrayList<>();
        _size = lexiconProfile.lexicalSize();
        _rName.append("c(");
        _rOcc.append("c(");
        _lexiconProfile.forEach(this::convertToRContext);
        closeRVariable();
    }

    /**
     * getter for the name of the column for R
     * @return return the R columns
     */
    public StringBuffer getRName() {
        return _rName;
    }

    /**
     * getter for the occurence of each words in R format
     * @return return a R variable
     */
    public StringBuffer getROcc() {
        return _rOcc;
    }

    /**
     * get the id of the _context Lexicon corresponds to the id of the CorpusLexicon class
     * @return the list of id of each words
     * @see CorpusLexicon
     */
    public List<String> getIdContextLexicon() {
        return _idContextLexicon;
    }

    /**
     * get the size of the _context or Corpus
     * @return
     */
    public int getSize() { return _size; }

    /**
     * used to closed a R Variable
     */
    private void closeRVariable() {
        _rName.deleteCharAt(_rName.length()-1);
        _rOcc.deleteCharAt(_rOcc.length()-1);
        _rName.append(")");
        _rOcc.append(")");
    }

    /**
     * used to convert a corpus
     * @param word a word
     */
    private void convertToRGlobal(String word) {
        _rName.append("\"").append(_corpus.getIdEntry(word)).append("\",");
        _rOcc.append(_corpus.count(word)).append(",");
    }

    /**
     * used to convert a contextLexicon
     * @param word a word
     */
    private void convertToRContext(String word) {
        _rName.append("\"").append(_corpus.getIdEntry(word)).append("\",");
        _rOcc.append(_lexiconProfile.count(word)).append(",");
        _idContextLexicon.add(_corpus.getIdEntry(word));
    }
}
