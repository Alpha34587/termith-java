package org.atilf.models;

import org.atilf.models.disambiguation.*;
import org.atilf.models.enrichment.TermOffsetId;

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
    private Path _corpus = null;
    private List<Path> _terminologies = new CopyOnWriteArrayList<>();
    private Map<String, Path> _tokenizeTeiBody = new ConcurrentHashMap<>();
    private Map<String, Path> _morphologyStandOff = new ConcurrentHashMap<>();
    private Map<String, List<TermOffsetId>> _terminologyStandOff = new ConcurrentHashMap<>();
    private Map<String, Path> _extractedText = new ConcurrentHashMap<>();
    private Map<String, Path> _xmlCorpus = new ConcurrentHashMap<>();
    private List<Path> _serializeJson = new CopyOnWriteArrayList<>();
    private List<Path> _outputFile = new CopyOnWriteArrayList<>();
    private int _corpusSize;

    /*
    Disambiguation core fields
     */

    private Map<String, LexiconProfile> _contextLexicon = new ConcurrentHashMap<>();
    private Map<String,Map<String, EvaluationProfile>> _evaluationLexicon = new ConcurrentHashMap<>();
    private CorpusLexicon _corpusLexicon = new CorpusLexicon(new ConcurrentHashMap<>(),new ConcurrentHashMap<>());
    private Map<String, Path> _learningTransformedFiles = new ConcurrentHashMap<>();
    private Map<String, Path> _evaluationTransformedFiles = new ConcurrentHashMap<>();
    private Map<String,ScoreTerm> _scoreTerms = new ConcurrentHashMap<>();
    private TotalTermScore _totalTermScore = new TotalTermScore();
    private Map<String, Path> _transformOutputDisambiguationFile = new ConcurrentHashMap<>();

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

    /**
     * return the size of the corpus
     * @return return the size of the corpus
     */
    public int getCorpusSize() {return _corpusSize;}

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

    public TotalTermScore getTotalTermScore() { return _totalTermScore; }

    /**
     * the path of input corpus
     * @return return the path
     */
    public Path getCorpus() {
        return _corpus;
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

    public Map<String, Path> getTransformOutputDisambiguationFile() {
        return _transformOutputDisambiguationFile;
    }

    public Map<String, ScoreTerm> getScoreTerms() {
        return _scoreTerms;
    }

    /*
    Setter
     */

    /**
     * set the size of the corpus
     * @param corpusSize set the size of the corpus
     */
    public void setCorpusSize(int corpusSize) {
        _corpusSize = corpusSize;
    }

    /**
     * set the path of the corpus
     * @param corpus the path of the corpus
     */
    public void setCorpus(Path corpus) {
        _corpus = corpus;
    }

    /**
     * set the terminologyStandOff
     * @param terminologyStandOff list of TermOffsetId
     * @see TermOffsetId
     */
    public void setTerminologyStandOff(Map<String, List<TermOffsetId>> terminologyStandOff) {
        _terminologyStandOff = terminologyStandOff;
    }
}
