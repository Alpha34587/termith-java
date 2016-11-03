package org.atilf.worker;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguisation.ContextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class ContextExtractorWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextExtractorWorker.class.getName());

    public ContextExtractorWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        LOGGER.info("add " + p + " to sub lexic");
        ContextExtractor contextExtractor = new ContextExtractor(p.toString(),termithIndex.getTermSubLexic());
        contextExtractor.execute();
        LOGGER.info(p + " added");
    }
}
