package org.atilf.models.termith;

import org.atilf.models.disambiguisation.GlobalLexic;
import org.atilf.models.termsuite.TermsOffsetId;
import org.atilf.module.disambiguisation.EvaluationProfile;
import org.atilf.module.disambiguisation.LexicalProfile;
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

    private static Path _outputPath;
    private static String _lang;
    private static String _treeTaggerHome;
    private static Path _base;
    private static boolean _keepFiles;

    public Path _corpus = null;
    public Path _disambAnnotation;
    private List<Path> _terminologies = new CopyOnWriteArrayList<>();
    private Map<String, Path> _tokenizeTeiBody = new ConcurrentHashMap<>();
    private Map<String, Path> _morphoSyntaxStandOff = new ConcurrentHashMap<>();
    private Map<String, List<TermsOffsetId>> _terminologyStandOff = new ConcurrentHashMap<>();
    private Map<String, Path> _extractedText = new ConcurrentHashMap<>();
    private Map<String, Path> _xmlCorpus = new ConcurrentHashMap<>();
    private Map<String, Path> _jsonTreeTagger = new ConcurrentHashMap<>();
    private List<Path> _serializeJson = new CopyOnWriteArrayList<>();
    private List<Path> _outputFile = new CopyOnWriteArrayList<>();
    private Map<String, LexicalProfile> _termSubLexic = new ConcurrentHashMap<>();
    private Map<String,Map<String, EvaluationProfile>> _evaluationLexic;
    private GlobalLexic _disambGlobalLexic = new GlobalLexic(new ConcurrentHashMap<>(),new ConcurrentHashMap<>());
    private int _corpusSize;

    private TermithIndex(Builder builder) throws IOException {
        _disambAnnotation = builder._disambAnnotation;
        _base = builder._base;
        _keepFiles = builder._keepFiles;
        _outputPath = builder._outputPath;
        _treeTaggerHome = builder._treeTaggerHome;
        _lang = builder._lang;
        _corpusSize = builder._corpusSize;
    }

    /*
    Getter
     */

    public List<Path> get_terminologies() {
        return _terminologies;
    }

    public Path getJsonTerminology(){
        return _terminologies.get(1);
    }

    public GlobalLexic get_disambGlobalLexic() { return _disambGlobalLexic; }

    public int get_corpusSize() {return _corpusSize;}

    public Map<String,Map<String, EvaluationProfile>> get_evaluationLexic() {
        return _evaluationLexic;
    }

    public Map<String, Path> get_morphoSyntaxStandOff() {
        return _morphoSyntaxStandOff;
    }

    public Map<String, List<TermsOffsetId>> get_terminologyStandOff() {
        return _terminologyStandOff;
    }

    public Map<String, Path> get_tokenizeTeiBody() {
        return _tokenizeTeiBody;
    }

    public List<Path> get_serializeJson() {return _serializeJson;}

    public Path get_corpus() {
        return _corpus;
    }

    public Map<String, Path> get_xmlCorpus() {
        return _xmlCorpus;
    }

    public List<Path> get_outputFile() {
        return _outputFile;
    }

    public Map<String, Path> get_jsonTreeTagger() {
        return _jsonTreeTagger;
    }

    public Map<String, LexicalProfile> get_termSubLexic() {
        return _termSubLexic;
    }

    public Map<String, Path> get_extractedText() {
        return _extractedText;
    }

    public static Path get_outputPath() { return _outputPath;}

    public static String get_lang() { return _lang; }

    public static String get_treeTaggerHome() { return _treeTaggerHome; }

    public static Path get_base() { return _base; }

    public static boolean is_keepFiles() { return _keepFiles; }

    public Path get_disambAnnotation() { return _disambAnnotation; }

    /*
    Setter
     */

    public void set_corpusSize(int corpusSize) {
        _corpusSize = corpusSize;
    }

    public void set_corpus(Path corpus) {
        _corpus = corpus;
    }

    public void set_terminologyStandOff(Map<String, List<TermsOffsetId>> terminologyStandOff) {
        _terminologyStandOff = terminologyStandOff;
    }

    /*
    Other method
     */
    public void addText(String id, StringBuilder content) throws IOException {

        this.get_extractedText().put(id, FilesUtils.writeObject(content,TermithIndex._outputPath));
    }

    public boolean iskeepFiles() {
        return _keepFiles;
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
        Path _disambAnnotation;
        private int _corpusSize = 0;

        /**
         * This method set the input folder path
         * @param path path of the _base corpus
         * @return return the path of the _base corpus
         * @throws IOException
         */
        public Builder baseFolder(String path) throws IOException {
            _base = FilesUtils.folderPathResolver(path);
            _corpusSize = (int) Files.list(_base).count();
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
         * @param disambAnnotation disamb path
         * @return return disamb path
         */
        public Builder annotation(String disambAnnotation) {
            _disambAnnotation = FilesUtils.folderPathResolver(disambAnnotation);
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
