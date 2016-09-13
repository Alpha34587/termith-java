package runner;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.Initializer;
import thread.TermSuiteTextInjector;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * This is the API class who run the different process of the termITH project. an object termITH is instanced with the
 * inner builder class
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TermithText {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermithText.class.getName());

    TermithIndex termithIndex;

    public TermithText(TermithIndex termithIndex) {
        this.termithIndex = termithIndex;
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
        Initializer initializer = new Initializer(poolSize, termithIndex);
        try {
            initializer.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }

        LOGGER.info("Starting Second Phase: TermSuite + XML injection");
        TermSuiteTextInjector termSuiteTextInjector = new TermSuiteTextInjector(poolSize, termithIndex);
        try {
            termSuiteTextInjector.execute();
        } catch (Exception e) {
            LOGGER.error("Error during execution of the termsuite and injection phase : ", e);
            exit(1);

        }
    }
}
