package runner;

import thread.Initializer;
import thread.TermithXmlInjector;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Termith {

    private static final Logger LOGGER = Logger.getLogger(Termith.class.getName());

    private String outputPath;
    private String lang;
    private String treeTaggerHome;
    private Path base;
    private boolean trace;

    /***
     * this is a part of the builder pattern
     * @param builder
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
     * @return
     */
    public String getOutputPath() {
        return outputPath;
    }


    /***
     * return the language of the corpus
     * @return
     */
    public String getLang() {
        return lang;
    }

    /**
     * This method execute the different tasks of the process. it executes two main phase sequentially :
     * 1) the text extraction 2) the analyze of termsuite and the injection of morphosyntaxes for each file of the corpus.
     * The second phase need to have the extracted text on input.
     * @throws IOException
     * @throws InterruptedException
     * @throws TransformerException
     * @throws ExecutionException
     */
    public void execute() throws IOException, InterruptedException, TransformerException, ExecutionException {

        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.log(Level.INFO, "Pool size set to: " + poolSize);
        LOGGER.log(Level.INFO, "Starting First Phase: Text extraction");
        Initializer initializer = new Initializer(poolSize, base);
        try {
            initializer.execute();
        } catch ( Exception e ) {
            //TODO stop execution due to previous errors.
        }

        LOGGER.log(Level.INFO, "Starting Second Phase: TermSuite + XML injection");
        TermithXmlInjector termithXmlInjector = new TermithXmlInjector(poolSize,
                initializer.getExtractedText(), initializer.getXmlCorpus(), treeTaggerHome, lang);
        try {
            termithXmlInjector.execute();
        } catch (Exception e) {
            //TODO stop execution due to previous errors.

        }
    }

    public static class Builder
    {
        Path base;
        boolean trace = false;
        String outputPath = null;
        String lang;
        String treeTaggerHome;

        /**
         * This method set the input folder path
         * @param path
         * @return
         * @throws IOException
         */
        public Builder baseFolder(String path) throws IOException {
            this.base = Paths.get(path);
            return this;
        }

        /**
         * this method set a boolean that used to activate or not the trace : each step of the process will be export to
         * the result folder
         * @param activate
         * @return
         */
        public Builder trace(boolean activate){
            this.trace = activate;
            return this;
        }

        /**
         * set the output path of the result
         * @param outputPath
         * @return
         */
        public Builder export(String outputPath){
            this.outputPath = outputPath;
            return this;
        }

        /**
         * set the lang
         * @param lang
         * @return
         */
        public Builder lang(String lang){
            this.lang = lang;
            return this;
        }

        /**
         * set the TreeTagger path
         * @param treeTaggerHome
         * @return
         */
        public Builder treeTaggerHome(String treeTaggerHome){
            this.treeTaggerHome = treeTaggerHome;
            return this;
        }

        /**
         * This method is used to finalize the building of the Termith Object
         * @return
         */
        public Termith build() {
            return  new Termith(this);
        }
    }
}
