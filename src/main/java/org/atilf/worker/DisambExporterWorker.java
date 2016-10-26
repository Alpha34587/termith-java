package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.tools.DisambTeiWriter;
import org.atilf.module.tools.FilesUtils;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import static org.atilf.module.tools.FilesUtils.nameNormalizer;

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
        String file = FilesUtils.nameNormalizer(p.toString());
        DisambTeiWriter disambTeiWriter = new DisambTeiWriter(
                file,
                termithIndex.getEvaluationLexic().get(
                        file
                )
        );
        disambTeiWriter.execute();
    }
}
