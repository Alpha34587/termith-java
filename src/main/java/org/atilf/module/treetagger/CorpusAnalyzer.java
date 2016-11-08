package org.atilf.module.treetagger;

import org.atilf.models.termsuite.TextAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzer {


    private Map<String,TextAnalyzer> _analyzedTexts  = new HashMap<>();
    private int _totalSize;
    private int _cumulSize;
    private boolean _lastDocs;
    private int _index;
    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusAnalyzer.class.getName());

    public CorpusAnalyzer(Map<String, StringBuilder> extractedText){
        LOGGER.debug("CorpusAnalyzer object building started");
        _totalSize = totalSize(extractedText);
        _cumulSize = 0;
        _lastDocs = false;
        _index = 1;

        extractedText.forEach((id,content) ->
        {

            int documentSize = documentSize(extractedText,id);
            int nbDocs = nbOfDocs(extractedText);
            _cumulSize += documentSize;
            if (_index == nbDocs){
                _lastDocs = true;
            }

            TextAnalyzer textAnalyzer =
                    new TextAnalyzer(
                            documentSize(extractedText,id),
                            nbDocs,
                            end(extractedText,id),
                            _index,
                            _cumulSize,
                            _totalSize,
                            _lastDocs
                    );

            _index++;
            _analyzedTexts.put(id,textAnalyzer);
        });
        LOGGER.debug("CorpusAnalyzer object building ended");
    }

    /**
     * @return return analyzedText fields
     */
    public Map<String, TextAnalyzer> getAnalyzedTexts() {
        return _analyzedTexts;
    }

    /**
     * @return return the size of the total corpus
     */
    public int totalSize(Map<String, StringBuilder> extractedText) {
        int totalSize = 0;
        byte[] bytesCt;
        for (StringBuilder text : extractedText.values()){
            totalSize += text.toString().getBytes().length + 1;
        }
        return totalSize;
    }

    /**
     * return the size of a document
     * @return
     */
    public int documentSize(Map<String, StringBuilder> extractedText, String id){
        return extractedText.get(id)
                .toString().getBytes().length + 1;
    }

    /**
     * return the number of document of the corpus
     * @return
     */
    public int nbOfDocs(Map<String, StringBuilder> extractedText){
        return extractedText.size();
    }

    /**
     * return the uima offset of a document
     * @return
     */
    public int end(Map<String, StringBuilder> extractedText, String id){
        return extractedText.get(id).toString().length();
    }
}
