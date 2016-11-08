package org.atilf.models.disambiguation;

import org.atilf.module.disambiguation.LexicalProfile;

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
    private GlobalLexicon _corpus;
    private LexicalProfile _subCorpus;
    private List<String> _idSubCorpus;

    public RLexicon(GlobalLexicon corpus){
        _corpus = corpus;
        corpusSizeOcc = corpus.size();
        _rName.append("c(");
        _rOcc.append("c(");
        _corpus.forEach(this::convertToRGlobal);
        closeRVariable();
    }

    public RLexicon(LexicalProfile subCorpus, GlobalLexicon corpus) {
        _subCorpus = subCorpus;
        _corpus = corpus;
        _idSubCorpus = new ArrayList<>();
        corpusSizeOcc = subCorpus.lexicalSize();
        _rName.append("c(");
        _rOcc.append("c(");
        _subCorpus.forEach(this::convertToRContext);
        closeRVariable();
    }

    public StringBuffer getRName() {
        return _rName;
    }

    public StringBuffer getROcc() {
        return _rOcc;
    }

    public List<String> getIdSubCorpus() {
        return _idSubCorpus;
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
        _rOcc.append(_subCorpus.countOccurrence(el)).append(",");
        _idSubCorpus.add(_corpus.getIdEntry(el));
    }
}
