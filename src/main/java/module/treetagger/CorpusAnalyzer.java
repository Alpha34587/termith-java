package module.treetagger;

import thread.Initializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzer {



    private Map<String,TextAnalyzer> analyzedTexts;
    private int totalSize;
    private int cumulSize;
    private boolean lastDocs;
    private int index;
    public CorpusAnalyzer(){};

    public CorpusAnalyzer(Initializer initializer){
        analyzedTexts = new HashMap<>();
        totalSize = totalSize(initializer);
        cumulSize = 0;
        lastDocs = false;
        index = 1;

        initializer.getExtractedText().forEach((id,content) ->
                {

                    int documentSize = documentSize(initializer,id);
                    int nbDocs = nbOfDocs(initializer);
                    cumulSize += documentSize;
                    if (index == nbDocs){
                        lastDocs = true;
                    }

                    TextAnalyzer textAnalyzer =
                            new TextAnalyzer(
                                    documentSize(initializer,id),
                                    nbDocs,
                                    end(initializer,id),
                                    index,
                                    cumulSize,
                                    totalSize,
                                    lastDocs
                            );

                    index++;
                    analyzedTexts.put(id,textAnalyzer);
                });
    }

    /**
     * @return return analyzedText fields
     */
    public Map<String, TextAnalyzer> getAnalyzedTexts() {
        return analyzedTexts;
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
