package org.atilf.module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 06/09/16.
 */
public class TextAnalyzer {

    private int documentSize;

    private int nbOfDocs;
    private int begin;
    private int end;
    private int docIndex;
    private int cumulSize;
    private int totalSize;
    private boolean isLastDoc;
    public TextAnalyzer(){}

    public TextAnalyzer(int documentSize,
                        int nbOfDocs,
                        int end,
                        int docIndex,
                        int cumulSize,
                        int totalSize,
                        boolean isLastDoc) {
        this.documentSize = documentSize;
        this.nbOfDocs = nbOfDocs;
        this.begin = 0;
        this.end = end;
        this.docIndex = docIndex;
        this.cumulSize = cumulSize;
        this.totalSize = totalSize;
        this.isLastDoc = isLastDoc;
    }

    public int getDocumentSize() {
        return documentSize;
    }

    public int getNbOfDocs() {
        return nbOfDocs;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getDocIndex() {
        return docIndex;
    }

    public int getCumulSize() {
        return cumulSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public boolean getIsLastDoc() {
        return isLastDoc;
    }
}
