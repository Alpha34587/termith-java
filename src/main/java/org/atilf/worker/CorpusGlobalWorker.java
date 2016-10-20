package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.GlobalCorpus;
import org.atilf.module.disambiguisation.SubLexic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class CorpusGlobalWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusGlobalWorker.class.getName());

    public CorpusGlobalWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }
    @Override
    public void run() {
        LOGGER.debug("extract occurence from " + p + " to global corpus lexic");
        GlobalCorpus globalCorpus = new GlobalCorpus(p.toString(),termithIndex.getDisambGlobalCorpus());
        LOGGER.debug(p + " added to global corpus");
        globalCorpus.execute();
    }
}
