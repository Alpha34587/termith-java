package org.atilf.models.disambiguation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexicon {
    private int corpusSizeOcc;
    private StringBuffer _rName = new StringBuffer();
    private StringBuffer _rOcc = new StringBuffer();
    private CorpusLexicon _corpus;
    private LexiconProfile _lexiconProfile;
    private List<String> _idContextLexicon;

    public RLexicon(CorpusLexicon corpus){
        _corpus = corpus;
        corpusSizeOcc = corpus.size();
        _rName.append("c(");
        _rOcc.append("c(");
        _corpus.forEach(this::convertToRGlobal);
        closeRVariable();
    }

    public RLexicon(LexiconProfile lexiconProfile, CorpusLexicon corpus) {
        _lexiconProfile = lexiconProfile;
        _corpus = corpus;
        _idContextLexicon = new ArrayList<>();
        corpusSizeOcc = lexiconProfile.lexicalSize();
        _rName.append("c(");
        _rOcc.append("c(");
        _lexiconProfile.forEach(this::convertToRContext);
        closeRVariable();
    }

    public StringBuffer getRName() {
        return _rName;
    }

    public StringBuffer getROcc() {
        return _rOcc;
    }

    public List<String> getIdContextLexicon() {
        return _idContextLexicon;
    }

    public int getCorpusSizeOcc() { return corpusSizeOcc; }

    private void closeRVariable() {
        _rName.deleteCharAt(_rName.length()-1);
        _rOcc.deleteCharAt(_rOcc.length()-1);
        _rName.append(")");
        _rOcc.append(")");
    }

    private void convertToRGlobal(String el) {
        _rName.append("\"").append(_corpus.getIdEntry(el)).append("\",");
        _rOcc.append(_corpus.count(el)).append(",");
    }

    private void convertToRContext(String el) {
        _rName.append("\"").append(_corpus.getIdEntry(el)).append("\",");
        _rOcc.append(_lexiconProfile.countOccurrence(el)).append(",");
        _idContextLexicon.add(_corpus.getIdEntry(el));
    }
}
