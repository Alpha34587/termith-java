package runner;

import eu.project.ttc.tools.TermSuitePipeline;
import thread.Initializer;
import thread.TermithXmlInjector;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Termith {

    private static Logger LOGGER = Logger.getLogger(Termith.class.getName());

    private String outputPath;
    private String lang;
    private String treeTaggerHome;

    private ExecutorService executorService;
    private Path base;
    private boolean trace;
    private Initializer initializer;
    private TermithXmlInjector termithXmlInjector;
    private TermSuitePipeline termSuitePipeline;

    public Termith() {}

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

    /**
     * This method execute the different tasks of the process
     * @throws IOException
     * @throws InterruptedException
     * @throws TransformerException
     * @throws ExecutionException
     */
    public void execute() throws IOException, InterruptedException, TransformerException, ExecutionException {

        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.log(Level.INFO, "Pool size set to: " + poolSize);
        LOGGER.log(Level.INFO, "Starting First Phase: Text extraction");
        initializer = new Initializer(poolSize, base);
        try {
            initializer.execute();
        } catch ( Exception e ) {
            //TODO stop execution due to previous errors.
        }

        LOGGER.log(Level.INFO, "Starting Second Phase: TermSuite + XML injection");
        TermithXmlInjector termithXmlInjector = new TermithXmlInjector(poolSize,
                initializer.getExtractedText(), treeTaggerHome, lang);
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

        public Builder baseFolder(String path) throws IOException {
            this.base = Paths.get(path);
            return this;
        }

        public Builder trace(boolean activate){
            this.trace = activate;
            return this;
        }

        public Builder export(String outputPath){
            this.outputPath = outputPath;
            return this;
        }

        public Builder lang(String lang){
            this.lang = lang;
            return this;
        }

        public Builder treeTaggerHome(String treeTaggerHome){
            this.treeTaggerHome = treeTaggerHome;
            return this;
        }

        public Termith build() {
            Termith termith =  new Termith(this);
            return termith;
        }
    }
}
