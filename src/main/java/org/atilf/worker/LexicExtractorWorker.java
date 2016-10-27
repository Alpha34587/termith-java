package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.LexicExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class LexicExtractorWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(LexicExtractorWorker.class.getName());

    public LexicExtractorWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }
    @Override
    public void run() {
        LOGGER.debug("extract occurence from " + p + " to global corpus lexic");
        LexicExtractor lexicExtractor = new LexicExtractor(p.toString(),termithIndex.get_disambGlobalLexic());
        LOGGER.debug(p + " added to global corpus");
        lexicExtractor.execute();
    }
}
