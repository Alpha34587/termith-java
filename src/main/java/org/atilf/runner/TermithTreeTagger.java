package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.enrichment.AnalyzeThread;
import org.atilf.thread.enrichment.CleanerThread;
import org.atilf.thread.enrichment.ExporterThread;
import org.atilf.thread.enrichment.InitializerThread;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TermithTreeTagger extends Runner {

    /**
     * this constructor initializes the _termithIndex field.
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public TermithTreeTagger(TermithIndex termithIndex) {
        super(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initializes the _termithIndex field and the number of thread used during the process
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     */
    public TermithTreeTagger(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
    }

    /**
     * The execute method of TermithTreeTagger has three phases :
     *      1) prepare the data : text extraction and keep the path of each corpus files
     *      2) analyze files : analyze the morphology of each files and transformed it on the termsuite format in order
     *      to analyze the terminology of the corpus and generate a terminology
     *      3) exporter : export the set of analysis into tei file format
     */
    @Override
    public void execute() throws IOException, InterruptedException {

        _logger.info("Pool size set to: " + _poolSize);
        /*
        Extraction text phase
         */
        _logger.info("First Phase Started : Text extraction");
        try{
            InitializerThread initializerThread = new InitializerThread(_termithIndex, _poolSize);
            initializerThread.execute();
        } catch ( Exception e ) {
            _logger.error("Error during execution of the extraction text phase : ", e);
            exit(1);
        }
        _logger.info("First Phase Finished : Text extraction");

        /*
        Morphology and terminology analysis phase
         */
        _logger.info("Starting Second Phase Started: Analyze Phase");
        AnalyzeThread analyzeThread = new AnalyzeThread(_termithIndex, _poolSize);
        try {
            analyzeThread.execute();
        } catch ( Exception e ) {
            _logger.error("Error during execution of the analyze phase : ", e);
            exit(1);
        }
        _logger.info("Starting Second Phase Finished: Analyze Phase");

        /*
        Tei exportation phase
         */
        _logger.info("Tei exportation phase starting :");
        try {
            ExporterThread exporter = new ExporterThread(_termithIndex, _poolSize);
            exporter.execute();
        } catch (Exception e) {
            _logger.error("Error during execution of the extraction text phase : ", e);
            exit(1);
        }

        /*
        Clean working directory
         */
        _logger.info("Cleaning working directory");
        try {
            CleanerThread cleaner = new CleanerThread();
            cleaner.execute();
        } catch (Exception e) {
            _logger.error("Error during execution of the extraction text phase : ", e);
            exit(1);
        }
    }

}
