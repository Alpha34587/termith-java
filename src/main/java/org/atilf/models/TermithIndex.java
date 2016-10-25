package org.atilf.models;

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

    public static Path outputPath;
    public static String lang;
    public static String treeTaggerHome;
    public static Path base;
    public static boolean keepFiles;

    public Path corpus;
    public Path disambAnnotation;
    private List<Path> terminologies;
    private Map<String, Path> tokenizeTeiBody;
    private Map<String, Path> morphoSyntaxStandOff;
    private Map<String, List<TermsOffsetId>> terminologyStandOff;
    private Map<String, Path> extractedText;
    private Map<String, Path> xmlCorpus;
    private Map<String, Path> JsonTreeTagger;
    private List<Path> SerializeJson;
    private List<Path> outputFile;
    private Map<String, LexicalProfile> termSubLexic;
    private Map<String,Map<String, EvaluationProfile>> evaluationLexic;
    private GlobalLexic disambGlobalLexic;

    private int corpusSize;

    private TermithIndex(Builder builder) throws IOException {
        disambAnnotation = builder.disambAnnotation;
        base = builder.base;
        keepFiles = builder.keepFiles;
        outputPath = builder.outputPath;
        treeTaggerHome = builder.treeTaggerHome;
        lang = builder.lang;
        terminologies = new CopyOnWriteArrayList<>();
        morphoSyntaxStandOff = new ConcurrentHashMap<>();
        tokenizeTeiBody = new ConcurrentHashMap<>();
        corpus = null;
        outputFile = new CopyOnWriteArrayList<>();
        extractedText = new ConcurrentHashMap<>();
        xmlCorpus = new ConcurrentHashMap<>();
        JsonTreeTagger = new ConcurrentHashMap<>();
        SerializeJson = new CopyOnWriteArrayList<>();
        terminologyStandOff = new ConcurrentHashMap<>();
        termSubLexic = new ConcurrentHashMap<>();
        disambGlobalLexic = new GlobalLexic(new ConcurrentHashMap<>(),new ConcurrentHashMap<>());
        corpusSize = builder.corpusSize;
    }

    public boolean iskeepFiles() {
        return keepFiles;
    }

    public List<Path> getTerminologies() {
        return terminologies;
    }

    public Path getJsonTerminology(){
        return terminologies.get(1);
    }

    public Map<String,Map<String, EvaluationProfile>> getEvaluationLexic() {
        return evaluationLexic;
    }

    public GlobalLexic getDisambGlobalLexic() { return disambGlobalLexic; }

    public int getCorpusSize() {return corpusSize;}

    public Map<String, Path> getMorphoSyntaxStandOff() {
        return morphoSyntaxStandOff;
    }

    public Map<String, List<TermsOffsetId>> getTerminologyStandOff() {
        return terminologyStandOff;
    }

    public Map<String, Path> getTokenizeTeiBody() {
        return tokenizeTeiBody;
    }

    public List<Path> getSerializeJson() {return SerializeJson;}

    public Path getCorpus() {
        return corpus;
    }

    public Map<String, Path> getExtractedText() {
        return extractedText;
    }

    public void addText(String id, StringBuilder content) throws IOException {

        this.getExtractedText().put(id, FilesUtils.writeObject(content,TermithIndex.outputPath));
    }

    public Map<String, LexicalProfile> getTermSubLexic() {
        return termSubLexic;
    }

    public void setCorpusSize(int corpusSize) {
        this.corpusSize = corpusSize;
    }

    public List<Path> getOutputFile() {
        return outputFile;
    }

    public Map<String, Path> getXmlCorpus() {
        return xmlCorpus;
    }

    public Map<String, Path> getJsonTreeTagger() {
        return JsonTreeTagger;
    }

    public void setTerminologyStandOff(Map<String, List<TermsOffsetId>> terminologyStandOff) {
        this.terminologyStandOff = terminologyStandOff;
    }

    public void setCorpus(Path corpus) {
        this.corpus = corpus;
    }

    /**
     * This class is used to instance a termITH object
     */
    public static class Builder
    {
        Path base;
        boolean keepFiles = false;
        Path outputPath = null;
        String lang;
        String treeTaggerHome;
        List<Path> terminology = new CopyOnWriteArrayList<>();
        Path disambAnnotation;
        private int corpusSize = 0;

        /**
         * This method set the input folder path
         * @param path path of the base corpus
         * @return return the path of the base corpus
         * @throws IOException
         */
        public Builder baseFolder(String path) throws IOException {
            this.base = FilesUtils.folderPathResolver(path);
            corpusSize = (int) Files.list(base).count();
            return this;
        }

        /**
         * this method set a boolean that used to activate or not the keepFiles : each step of the process will be export to
         * the result folder
         * @param activate boolean use to activate the "keepFiles" mode
         * @return value of the activate boolean
         */
        public Builder keepFiles(boolean activate){
            this.keepFiles = activate;
            return this;
        }

        /**
         * set the output path of the result
         * @param outputPath the output path
         * @return return output path
         */
        public Builder export(String outputPath){
            this.outputPath = FilesUtils.folderPathResolver(outputPath);
            return this;
        }

        /**
         * set the lang
         * @param lang set language
         * @return return string language
         */
        public Builder lang(String lang){
            this.lang = lang;
            return this;
        }

        /**
         * set the TreeTagger path
         * @param treeTaggerHome TreeTagger path
         * @return return TreeTagger path
         */
        public Builder treeTaggerHome(String treeTaggerHome) {
            this.treeTaggerHome = FilesUtils.folderPathResolver(treeTaggerHome).toString();
            return this;
        }

        /**
         * set json terminology path
         * @param terminology TreeTagger path
         * @return return TreeTagger path
         */
        public Builder terminology(String terminology) {
            this.terminology.add(null);
            this.terminology.add(FilesUtils.folderPathResolver(terminology));
            return this;
        }


        /**
         * set annotation path
         * @param disambAnnotation disamb path
         * @return return disamb path
         */
        public Builder annotation(String disambAnnotation) {
            this.disambAnnotation = FilesUtils.folderPathResolver(disambAnnotation);
            return this;
        }

        /**
         * This method is used to finalize the building of the TermithText Object
         * @see TermithIndex
         * @return return termith object
         */
        public TermithIndex build() throws IOException {
            return  new TermithIndex(this);
        }
    }
}
