package org.atilf.runner;

import org.atilf.thread.enrichment.CleanerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

import static java.lang.System.exit;

/**
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class Cleaner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Cleaner.class.getName());

    public Cleaner(){}


    public void execute() throws InterruptedException {
        LOGGER.info("Cleaning working directory");
        try{
            CleanerThread cleaner = new CleanerThread();
            cleaner.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }
    }
}
