package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.SubLexic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexicWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicWorker.class.getName());

    public SubLexicWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        LOGGER.debug("add " + p + " to sub lexic");
        SubLexic subLexic = new SubLexic(p.toString(),termithIndex.getTermSubLexic());
        LOGGER.debug(p + " added");
        subLexic.execute();
    }
}
