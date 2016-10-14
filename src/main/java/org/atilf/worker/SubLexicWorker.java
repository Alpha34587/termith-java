package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.SubLexic;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexicWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;

    public SubLexicWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        SubLexic subLexic = new SubLexic();
        subLexic.execute();
//        termithIndex.getSubLexics().put()
    }
}
