package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.tools.DisambTeiWriter;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambExporterWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;

    public DisambExporterWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        DisambTeiWriter disambTeiWriter = new DisambTeiWriter(p,termithIndex.getEvaluationLexic().get(p));
        disambTeiWriter.execute();
    }
}
