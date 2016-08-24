package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.Initializer;
import thread.TeiMorphologySyntaxGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

/**
 * This is the API class who run the different process of the termITH project. an object termITH is instanced with the
 * inner builder class
 * @see Builder
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class Termith {

    private static final Logger LOGGER = LoggerFactory.getLogger(Termith.class.getName());

    private String outputPath;
    private String lang;
    private String treeTaggerHome;
    private Path base;
    private boolean trace;

    /***
     * this is a part of the builder pattern
     * @param builder Builder object
     */
    private Termith(Builder builder){
        base = builder.base;
        trace = builder.trace;
        outputPath = builder.outputPath;
        treeTaggerHome = builder.treeTaggerHome;
        lang = builder.lang;
    }

    /***
     * return the outputPath parameter
     * @return return the path of the result of the analyze
     */
    public String getOutputPath() {
        return outputPath;
    }


    /***
     * return the language of the corpus
     * @return return the language of the corpus
     */
    public String getLang() {
        return lang;
    }

    /**
     * This method execute the different tasks of the process. it executes two main phase sequentially :
     * 1) the text extraction 2) the analyze of termsuite and the injection of morphosyntaxes for each file of the corpus.
     * The second phase need to have the extracted text on input.
     * @throws IOException this exception never occurs
     */
    public void execute() throws IOException {

        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Pool size set to: " + poolSize);
        LOGGER.info("Starting First Phase: Text extraction");
        Initializer initializer = new Initializer(poolSize, base);
        try {
            initializer.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }

        LOGGER.info("Starting Second Phase: TermSuite + XML injection");
        TeiMorphologySyntaxGenerator teiMorphologySyntaxGenerator = new TeiMorphologySyntaxGenerator(poolSize,
                initializer.getExtractedText(), initializer.getXmlCorpus(), treeTaggerHome, lang);
        try {
            teiMorphologySyntaxGenerator.execute();
        } catch (Exception e) {
            LOGGER.error("Error during execution of the termsuite and injection phase : ", e);
            exit(1);

        }
    }

    /**
     * This class is used to instance a termITH object
     */
    public static class Builder
    {
        Path base;
        boolean trace = false;
        String outputPath = null;
        String lang;
        String treeTaggerHome;

        /**
         * This method set the input folder path
         * @param path path of the base corpus
         * @return return the path of the base corpus
         * @throws IOException
         */
        public Builder baseFolder(String path) throws IOException {
            this.base = Paths.get(path);
            return this;
        }

        /**
         * this method set a boolean that used to activate or not the trace : each step of the process will be export to
         * the result folder
         * @param activate boolean use to activate the "trace" mode
         * @return value of the activate boolean
         */
        public Builder trace(boolean activate){
            this.trace = activate;
            return this;
        }

        /**
         * set the output path of the result
         * @param outputPath the output path
         * @return return output path
         */
        public Builder export(String outputPath){
            this.outputPath = outputPath;
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
        public Builder treeTaggerHome(String treeTaggerHome){
            this.treeTaggerHome = treeTaggerHome;
            return this;
        }

        /**
         * This method is used to finalize the building of the Termith Object
         * @see Termith
         * @return return termith object
         */
        public Termith build() {
            return  new Termith(this);
        }
    }
}
