package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.Initializer;
import thread.JsonWriterInjector;
import thread.TermSuiteJsonInjector;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TermithTreeTagger extends TermithText {
    /***
     * this is a part of the builder pattern

     * @param builder Builder object
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(TermithText.class.getName());

    private TermithTreeTagger(Builder builder) {
        base = builder.base;
        trace = builder.trace;
        outputPath = builder.outputPath;
        treeTaggerHome = builder.treeTaggerHome;
        lang = builder.lang;
    }

    @Override
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

        LOGGER.info("Starting Second Phase: Json Writer");
        JsonWriterInjector jsonWriterInjector = new JsonWriterInjector(poolSize,initializer,
                initializer.getXmlCorpus(),treeTaggerHome, lang);
        try {
            jsonWriterInjector.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }

        LOGGER.info("Starting Third Phase: TermSuite + XML injection");
        TermSuiteJsonInjector termSuiteJsonInjector = new TermSuiteJsonInjector(
                poolSize, jsonWriterInjector.getJsonTreeTagger(), initializer.getXmlCorpus(), treeTaggerHome, lang,
                jsonWriterInjector.getCorpus());
        try {
            termSuiteJsonInjector.execute();
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
         * This method is used to finalize the building of the TermithText Object
         * @see TermithTreeTagger
         * @return return termith object
         */
        public TermithTreeTagger build() {
            return  new TermithTreeTagger(this);
        }
    }
}
