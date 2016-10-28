package org.atilf.models.disambiguisation;

import org.atilf.models.disambiguisation.GlobalLexic;
import org.atilf.module.disambiguisation.LexicalProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexic {
    int corpusSizeOcc;
    private StringBuffer _rName = new StringBuffer();
    private StringBuffer _rOcc = new StringBuffer();
    private GlobalLexic _corpus;
    private LexicalProfile _subCorpus;
    private List<String> _idSubCorpus;

    public RLexic(GlobalLexic corpus){
        _corpus = corpus;
        corpusSizeOcc = corpus.size();
        _rName.append("c(");
        _rOcc.append("c(");
        _corpus.forEach(this::convertToRFormat);
        closeRVariable();
    }

    public RLexic(LexicalProfile subCorpus, GlobalLexic corpus) {
        _subCorpus = subCorpus;
        _corpus = corpus;
        _idSubCorpus = new ArrayList<>();
        corpusSizeOcc = subCorpus.lexicalSize();
        _rName.append("c(");
        _rOcc.append("c(");
        _subCorpus.forEach(this::convertToRFSubormat);
        closeRVariable();
    }

    public StringBuffer get_rName() {
        return _rName;
    }

    public StringBuffer get_rOcc() {
        return _rOcc;
    }

    public List<String> get_idSubCorpus() {
        return _idSubCorpus;
    }

    public int getCorpusSizeOcc() { return corpusSizeOcc; }

    private void closeRVariable() {
        _rName.deleteCharAt(_rName.length()-1);
        _rOcc.deleteCharAt(_rOcc.length()-1);
        _rName.append(")");
        _rOcc.append(")");
    }

    private void convertToRFormat(String el) {
        _rName.append("\""+ _corpus.get_idEntry(el) +"\",");
        _rOcc.append(_corpus.count(el)+",");
    }

    private void convertToRFSubormat(String el) {
        _rName.append("\""+ _corpus.get_idEntry(el) +"\",");
        _rOcc.append(_subCorpus.countOccurrence(el)+",");
        _idSubCorpus.add(_corpus.get_idEntry(el));
    }
}
