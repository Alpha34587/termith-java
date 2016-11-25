package org.atilf.models.termith;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.termsuite.TermOffsetId;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.module.tools.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
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
    Disambiguation  core fields
     */

    private Map<String, LexiconProfile> _contextLexicon = new ConcurrentHashMap<>();
    private Map<String,Map<String, EvaluationProfile>> _evaluationLexicon = new ConcurrentHashMap<>();
    private CorpusLexicon _corpusLexicon = new CorpusLexicon(new ConcurrentHashMap<>(),new ConcurrentHashMap<>());
    private Map<String, Path> _learningTransformedFiles = new ConcurrentHashMap<>();
    private Map<String, Path> _evaluationTransformedFiles = new ConcurrentHashMap<>();

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

    public List<Path> getTerminologies() {
        return _terminologies;
    }

    public Path getJsonTerminology(){
        return _terminologies.get(1);
    }

    public CorpusLexicon getCorpusLexicon() { return _corpusLexicon; }

    public int getCorpusSize() {return _corpusSize;}

    public Map<String,Map<String, EvaluationProfile>> getEvaluationLexicon() {
        return _evaluationLexicon;
    }

    public Map<String, Path> getMorphologyStandOff() {
        return _morphologyStandOff;
    }

    public Map<String, List<TermOffsetId>> getTerminologyStandOff() {return _terminologyStandOff;}

    public Map<String, Path> getTokenizeTeiBody() {
        return _tokenizeTeiBody;
    }

    public Map<String, Path> getLearningTransformedFile() {return _learningTransformedFiles;}

    public Map<String, Path> getEvaluationTransformedFiles() {return _evaluationTransformedFiles;}

    public List<Path> getSerializeJson() {return _serializeJson;}

    public static Path getLearningPath() {return _learningPath;}

    public static Path getEvaluationPath() {return _evaluationPath;}

    public Path getCorpus() {
        return _corpus;
    }

    public Map<String, Path> getXmlCorpus() {
        return _xmlCorpus;
    }

    public List<Path> getOutputFile() {
        return _outputFile;
    }

    public Map<String, Path> getJsonTreeTagger() {
        return _jsonTreeTagger;
    }

    public Map<String, LexiconProfile> getContextLexicon() {
        return _contextLexicon;
    }

    public Map<String, Path> getExtractedText() {
        return _extractedText;
    }

    public static Path getOutputPath() { return _outputPath;}

    public static String getLang() { return _lang; }

    public static String getTreeTaggerHome() { return _treeTaggerHome; }

    public static Path getBase() { return _base; }

    public static boolean isKeepFiles() { return _keepFiles; }

    public Path getDisambiguationAnnotation() { return _disambiguationAnnotation; }

    /*
    Setter
     */

    public void setCorpusSize(int corpusSize) {
        _corpusSize = corpusSize;
    }

    public void setCorpus(Path corpus) {
        _corpus = corpus;
    }

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
        public Builder export(String outputPath){
            _outputPath = FilesUtils.folderPathResolver(outputPath);
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
            _terminology.add(null);
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
