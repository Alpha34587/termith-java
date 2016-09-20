package org.atilf.module.treetagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzer {


    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusAnalyzer.class.getName());
    private Map<String,TextAnalyzer> analyzedTexts;
    private int totalSize;
    private int cumulSize;
    private boolean lastDocs;
    private int index;

    public CorpusAnalyzer(Map<String, StringBuffer> extractedText){
        LOGGER.debug("CorpusAnalyzer object building started");
        analyzedTexts = new HashMap<>();
        totalSize = totalSize(extractedText);
        cumulSize = 0;
        lastDocs = false;
        index = 1;

        extractedText.forEach((id,content) ->
                {

                    int documentSize = documentSize(extractedText,id);
                    int nbDocs = nbOfDocs(extractedText);
                    cumulSize += documentSize;
                    if (index == nbDocs){
                        lastDocs = true;
                    }

                    TextAnalyzer textAnalyzer =
                            new TextAnalyzer(
                                    documentSize(extractedText,id),
                                    nbDocs,
                                    end(extractedText,id),
                                    index,
                                    cumulSize,
                                    totalSize,
                                    lastDocs
                            );

                    index++;
                    analyzedTexts.put(id,textAnalyzer);
                });
        LOGGER.debug("CorpusAnalyzer object building ended");
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
    public int totalSize(Map<String, StringBuffer> extractedText) {
        int totalSize = 0;
        byte[] bytesCt;
        for (StringBuffer text : extractedText.values()){
            totalSize += text.toString().getBytes().length + 1;
        }
        return totalSize;
    }

    /**
     * return the size of a document
     * @return
     */
    public int documentSize(Map<String, StringBuffer> extractedText, String id){
        return extractedText.get(id)
                .toString().getBytes().length + 1;
    }

    /**
     * return the number of document of the corpus
     * @return
     */
    public int nbOfDocs(Map<String, StringBuffer> extractedText){
        return extractedText.size();
    }

    /**
     * return the uima offset of a document
     * @return
     */
    public int end(Map<String, StringBuffer> extractedText, String id){
        return extractedText.get(id).toString().length();
    }
}
