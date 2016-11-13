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

    public TermithTreeTagger(TermithIndex termithIndex) throws IOException {
        this(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }


    public TermithTreeTagger(TermithIndex termithIndex,int poolSize) throws IOException {
        _poolSize = poolSize;
        _termithIndex = termithIndex;

    }

    public TermithIndex getTermithIndex() {
        return _termithIndex;
    }

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
