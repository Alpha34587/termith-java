package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.Initializer;
import thread.JsonWriter;
import thread.TermSuiteJsonInjector;

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
        super(builder);
    }

    @Override
    public void execute(){

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
        JsonWriter jsonWriter = new JsonWriter();
        try {
            jsonWriter.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }

        LOGGER.info("Starting Third Phase: TermSuite + XML injection");
        TermSuiteJsonInjector termSuiteJsonInjector = new TermSuiteJsonInjector();
        try {
            termSuiteJsonInjector.execute();
        } catch (Exception e) {
            LOGGER.error("Error during execution of the termsuite and injection phase : ", e);
            exit(1);

        }
    }
}
