package org.atilf.models;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.TxmContext;
import org.atilf.models.enrichment.PhraseoOffsetId;
import org.atilf.models.enrichment.TermOffsetId;
import org.atilf.models.enrichment.TransdisciplinaryOffsetId;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * this the main class of the project. All the results of the different modules is in this class.
 * @author Simon Meoni
 *         Created on 13/09/16.
 */
public class TermithIndex {

    /*
    Termith fields
     */
    private List<Path> _terminologies = new CopyOnWriteArrayList<>();
    private Map<String, Path> _tokenizeTeiBody = new ConcurrentHashMap<>();
    private Map<String, Path> _morphologyStandOff = new ConcurrentHashMap<>();
    private Map<String, List<TermOffsetId>> _terminologyStandOff = new ConcurrentHashMap<>();
    private Map<String, Path> _extractedText = new ConcurrentHashMap<>();
    private Map<String, Path> _xmlCorpus = new ConcurrentHashMap<>();
    private List<Path> _serializeJson = new CopyOnWriteArrayList<>();
    private List<Path> _outputFile = new CopyOnWriteArrayList<>();
    private Map<String, List<TransdisciplinaryOffsetId>> _transOffetId = new ConcurrentHashMap<>();
    private Map<String, List<PhraseoOffsetId>> _phraseoOffetId = new ConcurrentHashMap<>();
    /*
    Disambiguation core fields
     */

    private Map<String, LexiconProfile> _contextLexicon = new ConcurrentHashMap<>();
    private Map<String,Map<String, EvaluationProfile>> _evaluationLexicon = new ConcurrentHashMap<>();
    private Map<String, List<TxmContext>> _TermsTxmContext = new ConcurrentHashMap<>();
    private CorpusLexicon _corpusLexicon = new CorpusLexicon(new ConcurrentHashMap<>(),new ConcurrentHashMap<>());
    private Map<String, Path> _learningTransformedFiles = new ConcurrentHashMap<>();
    private Map<String, Path> _evaluationTransformedFiles = new ConcurrentHashMap<>();

    /*
    Constructor
     */

    public TermithIndex(){}

    /*
    Getter
     */

    /**
     * get the path of terminologies in tbx or json
     * @return return the list of paths
     */
    public List<Path> getTerminologies() {
        return _terminologies;
    }

    /**
     * return the jsonTerminology
     * @return return a Path
     */
    public Path getJsonTerminology(){
        return _terminologies.get(1);
    }

    /**
     * return the corpusLexicon
     * @return return a corpusLexicon object
     */
    public CorpusLexicon getCorpusLexicon() { return _corpusLexicon; }

    public Map<String, List<TransdisciplinaryOffsetId>> getTransOffetId() {
        return _transOffetId;
    }

    public Map<String, List<PhraseoOffsetId>> getPhraseoOffetId() {
        return _phraseoOffetId;
    }

    /**
     * return the map who contains for each file a map of terms associated to evaluations profiles
     * @return return a Map<String,Map<String, EvaluationProfile>>
     */
    public Map<String,Map<String, EvaluationProfile>> getEvaluationLexicon() {
        return _evaluationLexicon;
    }

    /**
     * return serialized MorphologyStandOff object
     * @return return Map of String/Path
     */
    public Map<String, Path> getMorphologyStandOff() {
        return _morphologyStandOff;
    }

    /**
     * return the deserialize terminology
     * @return return a Map<String, List<TermOffsetId>>
     */
    public Map<String, List<TermOffsetId>> getTerminologyStandOff() {
        return _terminologyStandOff;
    }

    /**
     * return the Tokenize bodies of xml files
     * @return return a map of String/Path
     */
    public Map<String, Path> getTokenizeTeiBody() {
        return _tokenizeTeiBody;
    }

    /**
     * return the list of transformed xml files
     * @return return a list of path
     */
    public Map<String, Path> getLearningTransformedFile() {return _learningTransformedFiles;}

    /**
     * return the list of transformed xml files
     * @return return a list of path
     */
    public Map<String, Path> getEvaluationTransformedFiles() {return _evaluationTransformedFiles;}

    /**
     * return the list of serialized json file
     * @return return a list of path
     */
    public List<Path> getSerializeJson() {return _serializeJson;}

    public Map<String, List<TxmContext>> getTermsTxmContext() {
        return _TermsTxmContext;
    }

    /**
     * get the input xml files
     * @return the map of String/Path
     */
    public Map<String, Path> getXmlCorpus() {
        return _xmlCorpus;
    }

    /**
     * get the list of exported file
     * @return return the list of exported file
     */
    public List<Path> getOutputFile() {
        return _outputFile;
    }

    /**
     * return the map who contains pairs of Term Entry/LexiconProfile
     * @return return a map of String/LexiconProfile
     */
    public Map<String, LexiconProfile> getContextLexicon() {
        return _contextLexicon;
    }

    /**
     * return the map who contains the path of extracted text
     * @return return the map of String/Path
     */
    public Map<String, Path> getExtractedText() {
        return _extractedText;
    }

    /*
    Setter
     */

    /**
     * set the terminologyStandOff
     * @param terminologyStandOff list of TermOffsetId
     * @see TermOffsetId
     */
    public void setTerminologyStandOff(Map<String, List<TermOffsetId>> terminologyStandOff) {
        _terminologyStandOff = terminologyStandOff;
    }
}
