package runner;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.AnalyzeThread;
import thread.InitializerThread;
import thread.TermSuiteJsonInjector;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class OldTermithTreeTagger {
    /***
     * this is a part of the builder pattern

     * @param builder Builder object
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(TermithText.class.getName());
    private TermithIndex termithIndex;

    public OldTermithTreeTagger(TermithIndex termithIndex) {
        this.termithIndex = termithIndex;
    }

    public TermithIndex getTermithIndex() {
        return termithIndex;
    }

    public void execute() throws IOException {

        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Pool size set to: " + poolSize);
        LOGGER.info("Starting First Phase: Text extraction");
        InitializerThread initializerThread = new InitializerThread(poolSize, termithIndex);
        try {
            initializerThread.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }

        LOGGER.info("Starting Second Phase: Json Writer");
        AnalyzeThread analyzeThread = new AnalyzeThread(poolSize, termithIndex);
        try {
            analyzeThread.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }

        LOGGER.info("Starting Third Phase: TermSuite + XML injection");
        TermSuiteJsonInjector termSuiteJsonInjector = new TermSuiteJsonInjector(poolSize, termithIndex);
        try {
            termSuiteJsonInjector.execute();
        } catch (Exception e) {
            LOGGER.error("Error during execution of the termsuite and injection phase : ", e);
            exit(1);

        }
    }
}
