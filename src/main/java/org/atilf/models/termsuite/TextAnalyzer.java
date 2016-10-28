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

    public int get_documentSize() {
        return _documentSize;
    }

    public int get_nbOfDocs() {
        return _nbOfDocs;
    }

    public int get_begin() {
        return _begin;
    }

    public int get_end() {
        return _end;
    }

    public int get_docIndex() {
        return _docIndex;
    }

    public int get_cumulSize() {
        return _cumulSize;
    }

    public int get_totalSize() {
        return _totalSize;
    }

    public boolean getIsLastDoc() {
        return _isLastDoc;
    }
}
