package org.atilf.module.tools;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class WorkingFilesCleaner {
    private final Path corpus;
    private final boolean keepFiles;

    public WorkingFilesCleaner(Path corpus, boolean keepFiles) {

        this.corpus = corpus;
        this.keepFiles = keepFiles;
    }

    public void execute() {
    }
}
