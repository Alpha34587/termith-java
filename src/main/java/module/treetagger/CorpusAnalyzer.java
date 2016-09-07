package module.treetagger;

import thread.Initializer;

/**
 * @author Simon Meoni
 *         Created on 06/09/16.
 */
public class CorpusAnalyzer {

    int documentSize;
    int nbOfDocs;
    int documentOffset;
    int docIndex;
    int cumulSize;
    int totalSize;
    boolean isLastDoc;

    public CorpusAnalyzer(){}

    public CorpusAnalyzer(Initializer initializer){
        this.documentSize = documentSize(initializer);
        this.nbOfDocs = nbOfDocs(initializer);
        this.documentOffset = documentOffset(initializer);
        this.docIndex = docIndex(initializer);
        this.cumulSize = cumulSize(initializer);
        this.totalSize = totalSize(initializer);
        this.isLastDoc = isLastDoc(initializer);
    }

    /**
     * @return return the size of the total corpus
     */
    public int totalSize(Initializer initializer) {
        int totalOffset = -1;
        for (StringBuffer text : initializer.getExtractedText().values())
            totalOffset = totalOffset + text.length() + 1;

        return totalOffset;
    }

    /**
     * return the size of a document
     * @return
     */
    public int documentSize(Initializer initializer){
        return -1;
    }

    /**
     * return the number of document of the corpus
     * @return
     */
    public int nbOfDocs(Initializer initializer){
        return -1;
    }

    /**
     * return the uima offset of a document
     * @return
     */
    public int documentOffset(Initializer initializer){
        return -1;
    }

    /**
     * return the index number of a document
     * @return
     */
    public int docIndex(Initializer initializer){
        return -1;
    }

    /**
     * return true if the document is the last document of the extractedText field
     * @return
     */
    public boolean isLastDoc(Initializer initializer){
        return false;
    }

    /**
     * return the UIMA cumulSize of a document
     */
    public int cumulSize(Initializer initializer) {
        return -1;
    }

    /**
     * the initializerWorker have a run method who call a textExtractor object
     * @see TextExtractor
     */
}
