package runner;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.AnalyzeThread;
import thread.InitializerThread;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TermithTreeTagger {
    private static final Logger LOGGER = LoggerFactory.getLogger(TermithTreeTagger.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private int poolSize;
    private TermithIndex termithIndex;

    public TermithTreeTagger(TermithIndex termithIndex) throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }


    public TermithTreeTagger(int poolSize,TermithIndex termithIndex) throws IOException {
        this.poolSize = poolSize;
        this.termithIndex = termithIndex;

    }

    public TermithIndex getTermithIndex() {
        return termithIndex;
    }

    public void execute() throws IOException, InterruptedException {

        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Pool size set to: " + poolSize);
        LOGGER.info("First Phase Started : Text extraction");
        try{
            InitializerThread initializerThread = new InitializerThread(poolSize,termithIndex);
            initializerThread.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }
        LOGGER.info("First Phase Finished : Text extraction");

        LOGGER.info("Starting Second Phase Started: Json Writer");
        AnalyzeThread analyzeThread = new AnalyzeThread(poolSize, termithIndex);
        try {
            analyzeThread.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }


    }

}
