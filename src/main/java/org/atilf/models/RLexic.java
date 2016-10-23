package org.atilf.models;

import com.google.common.collect.Multiset;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexic {
    int corpusSizeOcc;
    private StringBuffer rName;
    private StringBuffer rOcc;
    private GlobalLexic corpus;
    private Multiset<String> subCorpus;

    public RLexic(GlobalLexic corpus){
        this.corpus = corpus;
        corpusSizeOcc = corpus.size();
        rName = new StringBuffer();
        rOcc = new StringBuffer();
        rName.append("c(");
        rOcc.append("c(");
        this.corpus.forEach(this::convertToRFormat);
        closeRVariable();
    }

    public RLexic(Multiset<String> subCorpus, GlobalLexic corpus) {
        this.subCorpus = subCorpus;
        this.corpus = corpus;
        corpusSizeOcc = subCorpus.size();
        rName = new StringBuffer();
        rOcc = new StringBuffer();
        rName.append("c(");
        rOcc.append("c(");
        subCorpus.forEach(this::convertToRFSubormat);
        closeRVariable();
    }

    public StringBuffer getrName() {
        return rName;
    }

    public StringBuffer getrOcc() {
        return rOcc;
    }

    public int getCorpusSizeOcc() { return corpusSizeOcc; }

    private void closeRVariable() {
        rName.deleteCharAt(rName.length()-1);
        rOcc.deleteCharAt(rOcc.length()-1);
        rName.append(")");
        rOcc.append(")");
    }

    private void convertToRFormat(String el) {
        rName.append("\""+ corpus.getIdEntry(el) +"\",");
        rOcc.append(corpus.getCount(el)+",");
    }

    private void convertToRFSubormat(String el) {
        rName.append("\""+ corpus.getIdEntry(el) +"\",");
        rOcc.append(subCorpus.count(el)+",");
    }
}
