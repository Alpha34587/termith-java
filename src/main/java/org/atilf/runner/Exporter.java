package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.thread.enrichment.ExporterThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;


public class Exporter {

    private TermithIndex _termithIndex;
    private int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public Exporter(TermithIndex termithIndex){
        this(termithIndex, POOL_SIZE);
    }

    public Exporter(TermithIndex termithIndex, int poolSize){
        _termithIndex = termithIndex;
        _poolSize = poolSize;
    }

    public void execute() throws InterruptedException {
        LOGGER.info("Tei exportation phase starting :");
        try{
            ExporterThread exporter = new ExporterThread(_poolSize, _termithIndex);
            exporter.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }
    }
}
