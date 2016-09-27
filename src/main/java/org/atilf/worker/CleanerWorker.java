package org.atilf.worker;

import org.atilf.module.tools.WorkingFilesCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;


/**
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class CleanerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanerWorker.class.getName());
    private final Path corpus;
    private final boolean keepFiles;

    public CleanerWorker(Path corpus, boolean keepFiles){
        this.corpus = corpus;
        this.keepFiles = keepFiles;
    }

    @Override
    public void run() {

        LOGGER.debug("clean working directory : " + this.corpus);
        WorkingFilesCleaner workingFilesCleaner = new WorkingFilesCleaner(this.corpus,this.keepFiles);
        try {
            workingFilesCleaner.execute();
        } catch (IOException e) {
            LOGGER.error("error during cleaning directory",e);
        }
    }
}
