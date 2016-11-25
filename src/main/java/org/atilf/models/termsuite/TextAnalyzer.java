package org.atilf.models.termsuite;

/**
 * the text analyzer is the metadata of one file needed for serialize json file
 * @author Simon Meoni
 *         Created on 06/09/16.
 */
public class TextAnalyzer {

    private int _documentSize;
    private int _nbOfDocs;
    private int _begin = 0;
    private int _end;
    private int _docIndex;
    private int _sumSize;
    private int _totalSize;
    private boolean _isLastDoc;

    /**
     * the empty constructor of TextAnalyzer
     */
    public TextAnalyzer(){}

    /**
     * the constructor of TextAnalyzer, this constructor set all the parameter fields of this class
     * @param documentSize the number of character of the document
     * @param nbOfDocs the the number of document of the corpus
     * @param end the end offset of the document
     * @param docIndex the index of the document used by the terminology
     * @param sumSize the cumulated size of the corpus
     * @param totalSize the size of the corpus
     * @param isLastDoc if the document is last document of the corpus
     */
    public TextAnalyzer(int documentSize,
                        int nbOfDocs,
                        int end,
                        int docIndex,
                        int sumSize,
                        int totalSize,
                        boolean isLastDoc) {
        _documentSize = documentSize;
        _nbOfDocs = nbOfDocs;
        _end = end;
        _docIndex = docIndex;
        _sumSize = sumSize;
        _totalSize = totalSize;
        _isLastDoc = isLastDoc;
    }

    /**
     * @return return the document size
     */
    public int getDocumentSize() {
        return _documentSize;
    }

    /**
     * @return return the number of docs of the corpus
     */
    public int getNbOfDocs() {
        return _nbOfDocs;
    }

    /**
     * @return return the beginning offset value of text (default value = 0)
     */
    public int getBegin() {
        return _begin;
    }

    /**
     * @return return the ending offset value of text
     */
    public int getEnd() {
        return _end;
    }

    /**
     * @return return the id of the corpus
     */
    public int getDocIndex() {
        return _docIndex;
    }

    /**
     * @return return the cumulated size of the corpus
     */
    public int getCumulatedSize() {
        return _sumSize;
    }

    /**
     * @return return the size of the corpus
     */
    public int getTotalSize() {
        return _totalSize;
    }

    /**
     * @return return lastDoc boolean value
     */
    public boolean getIsLastDoc() {
        return _isLastDoc;
    }
}
