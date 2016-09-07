package module.treetagger;

import thread.Initializer;

/**
 * @author Simon Meoni
 *         Created on 06/09/16.
 */
public class CorpusAnalyzer {

    int documentSize;
    int nbOfDocs;
    int begin;
    int end;
    int docIndex;
    int cumulSize;
    int totalSize;
    boolean isLastDoc;

    public CorpusAnalyzer(){}

    public CorpusAnalyzer(Initializer initializer, String id, int docIndex){
        this.documentSize = documentSize(initializer, id);
        this.nbOfDocs = nbOfDocs(initializer);
        this.begin = 0;
        this.end = end(initializer, id);
        this.docIndex = docIndex;
        this.cumulSize = cumulSize(initializer, id);
        this.totalSize = totalSize(initializer);
        this.isLastDoc = isLastDoc(initializer);
    }

    /**
     * @return return the size of the total corpus
     */
    public int totalSize(Initializer initializer) {
        int totalSize = 0;
        byte[] bytesCt;
        for (StringBuffer text : initializer.getExtractedText().values()){
            totalSize += text.toString().getBytes().length + 1;
        }
        return totalSize;
    }

    /**
     * return the size of a document
     * @return
     */
    public int documentSize(Initializer initializer, String id){
        return initializer.getExtractedText().get(id)
                .toString().getBytes().length + 1;
    }

    /**
     * return the number of document of the corpus
     * @return
     */
    public int nbOfDocs(Initializer initializer){
        return initializer.getExtractedText().size();
    }

    /**
     * return the uima offset of a document
     * @return
     */
    public int end(Initializer initializer, String id){
        return initializer.getExtractedText().get(id).toString().length();
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
    public int cumulSize(Initializer initializer, String id) {
        return -1;
    }

    /**
     * the initializerWorker have a run method who call a textExtractor object
     * @see TextExtractor
     */
}
