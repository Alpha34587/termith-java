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
    private Multiset<String> corpus;

    public RLexic(Multiset<String> corpus){
        this.corpus = corpus;
        corpusSizeOcc = corpus.size();
        rName = new StringBuffer();
        rOcc = new StringBuffer();
        rName.append("c(");
        rOcc.append("c(");
        corpus.elementSet().forEach(this::convertToRFormat);
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
        rName.append("\""+el+"\",");
        rOcc.append(corpus.count(el)+",");
    }
}
