package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.SubLexicExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexicExtractorWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicExtractorWorker.class.getName());

    public SubLexicExtractorWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        LOGGER.debug("add " + p + " to sub lexic");
        SubLexicExtractor subLexicExtractor = new SubLexicExtractor(p.toString(),termithIndex.getTermSubLexic());
        LOGGER.debug(p + " added");
        subLexicExtractor.execute();
    }
}
