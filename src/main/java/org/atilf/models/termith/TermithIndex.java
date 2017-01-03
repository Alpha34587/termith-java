package org.atilf.models.termith;

import org.apache.commons.io.FileUtils;
import org.atilf.models.disambiguation.*;
import org.atilf.models.termsuite.TermOffsetId;
import org.atilf.module.tools.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private Path _disambiguationAnnotation;

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
    CLI parameter
     */

    private static Path _outputPath;
    private static String _lang;
    private static String _treeTaggerHome;
    private static Path _base;
    private static boolean _keepFiles;

    /*
    Disambiguation CLI parameter
     */
    private static Path _learningPath;
    private static Path _evaluationPath;

    /*
    Constructor
     */

    private TermithIndex(Builder builder) throws IOException {
        _disambiguationAnnotation = builder._disambiguationAnnotation;
        _base = builder._base;
        _keepFiles = builder._keepFiles;
        _outputPath = builder._outputPath;
        _treeTaggerHome = builder._treeTaggerHome;
        _lang = builder._lang;
        _corpusSize = builder._corpusSize;
        _learningPath = builder._learningPath;
        _evaluationPath = builder._evaluationPath;
    }

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

    public static Path getLearningPath() {return _learningPath;}

    public static Path getEvaluationPath() {return _evaluationPath;}

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

    /**
     * get the output path
     * @return return the output path
     */
    public static Path getOutputPath() { return _outputPath;}

    public Map<String, Path> getTransformOutputDisambiguationFile() {
        return _transformOutputDisambiguationFile;
    }

    /**
     * get the corpus language
     * @return return the language corpus
     */
    public static String getLang() { return _lang; }

    /**
     * get the path of treetagger executable
     * @return the path of treetagger
     */
    public static String getTreeTaggerHome() { return _treeTaggerHome; }

    /**
     * get the path of the input folder
     * @return return the input folder path
     */
    public static Path getBase() { return _base; }

    /**
     * get boolean for clean or not working folder
     * @return return a boolean
     */
    public static boolean isKeepFiles() { return _keepFiles; }

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

    /*
    Other method
     */
    public void addText(String id, StringBuilder content) throws IOException {

        this.getExtractedText().put(id, FilesUtils.writeObject(content,TermithIndex._outputPath));
    }

    /**
     * This class is used to instance a termITH object
     */
    public static class Builder
    {
        Path _outputPath = null;
        boolean _keepFiles = false;
        List<Path> _terminology = new CopyOnWriteArrayList<>();
        String _lang;
        Path _base;
        String _treeTaggerHome;
        Path _disambiguationAnnotation;
        Path _learningPath;
        Path _evaluationPath;
        private int _corpusSize = 0;

        /**
         * This method set the input folder path
         * @param path path of the _base corpus
         * @return return the path of the _base corpus
         * @throws IOException throws exception of folderResolver method
         */
        public Builder baseFolder(String path) throws IOException {
            _base = FilesUtils.folderPathResolver(path);
            _corpusSize = (int) Files.list(_base).count();
            return this;
        }

        /**
         * This method set the learning folder path
         * @param path path of the _learningPath corpus
         * @return return the path of the _learningPath corpus
         * @throws IOException throws exception of folderResolver method
         */
        public Builder learningFolder(String path) throws IOException {
            _learningPath = FilesUtils.folderPathResolver(path);
            return this;
        }

        /**
         * This method set the evaluation folder path
         * @param path path of the _evaluationPath corpus
         * @return return the path of the _evaluationPath corpus
         * @throws IOException throws exception of folderResolver method
         */
        public Builder evaluationFolder(String path) throws IOException {
            _evaluationPath = FilesUtils.folderPathResolver(path);
            _corpusSize = (int) Files.list(_evaluationPath).count();
            return this;
        }

        /**
         * this method set a boolean that used to activate or not the _keepFiles : each step of the process will be export to
         * the result folder
         * @param activate boolean use to activate the "_keepFiles" mode
         * @return value of the activate boolean
         */
        public Builder keepFiles(boolean activate){
            _keepFiles = activate;
            return this;
        }

        /**
         * set the output path of the result
         * @param outputPath the output path
         * @return return output path
         */
        public Builder export(String outputPath) throws IOException {
            _outputPath = FilesUtils.folderPathResolver(outputPath);
            File folder = _outputPath.toFile();
            if (Files.exists(_outputPath)){
                FileUtils.deleteDirectory(folder);
            }
            if (!folder.mkdir()){
                throw new IOException("cannot create output folder");
            }
            return this;
        }

        /**
         * set the _lang
         * @param lang set language
         * @return return string language
         */
        public Builder lang(String lang){
            _lang = lang;
            return this;
        }

        /**
         * set the TreeTagger path
         * @param treeTaggerHome TreeTagger path
         * @return return TreeTagger path
         */
        public Builder treeTaggerHome(String treeTaggerHome) {
            _treeTaggerHome = FilesUtils.folderPathResolver(treeTaggerHome).toString();
            return this;
        }

        /**
         * set json terminology path
         * @param terminology TreeTagger path
         * @return return TreeTagger path
         */
        public Builder terminology(String terminology) {
            if (terminology == null)
                _terminology.add(null);
            else
                _terminology.add(FilesUtils.folderPathResolver(terminology));
            return this;
        }


        /**
         * set annotation path
         * @param disambiguationAnnotation disambiguation path
         * @return return disambiguation path
         */
        public Builder annotation(String disambiguationAnnotation) {
            _disambiguationAnnotation = FilesUtils.folderPathResolver(disambiguationAnnotation);
            return this;
        }

        /**
         * This method is used to finalize the building of the TermithText Object
         * @see TermithIndex
         * @return return termith object
         */
        public TermithIndex build() throws IOException {
            return new TermithIndex(this);
        }
    }
}
