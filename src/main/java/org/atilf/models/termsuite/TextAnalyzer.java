package org.atilf.models.termsuite;

/**
 * @author Simon Meoni
 *         Created on 06/09/16.
 */
public class TextAnalyzer {

    private int _documentSize;
    private int _nbOfDocs;
    private int _begin = 0;
    private int _end;
    private int _docIndex;
    private int _cumulSize;
    private int _totalSize;
    private boolean _isLastDoc;
    public TextAnalyzer(){}

    public TextAnalyzer(int documentSize,
                        int nbOfDocs,
                        int end,
                        int docIndex,
                        int cumulSize,
                        int totalSize,
                        boolean isLastDoc) {
        _documentSize = documentSize;
        _nbOfDocs = nbOfDocs;
        _end = end;
        _docIndex = docIndex;
        _cumulSize = cumulSize;
        _totalSize = totalSize;
        _isLastDoc = isLastDoc;
    }

    public int getDocumentSize() {
        return _documentSize;
    }

    public int getNbOfDocs() {
        return _nbOfDocs;
    }

    public int getBegin() {
        return _begin;
    }

    public int getEnd() {
        return _end;
    }

    public int getDocIndex() {
        return _docIndex;
    }

    public int getCumulSize() {
        return _cumulSize;
    }

    public int getTotalSize() {
        return _totalSize;
    }

    public boolean getIsLastDoc() {
        return _isLastDoc;
    }
}
