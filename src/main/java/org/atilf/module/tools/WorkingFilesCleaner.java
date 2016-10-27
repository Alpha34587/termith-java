package org.atilf.module.tools;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class WorkingFilesCleaner {
    private final Path _corpus;
    private final boolean _keepFiles;
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingFilesCleaner.class.getName());

    public WorkingFilesCleaner(Path corpus, boolean keepFiles) {
        _corpus = corpus;
        _keepFiles = keepFiles;
    }

    public void execute() throws IOException {
        Files.list(_corpus).forEach(
                path -> {
                    File file = new File(path.toString());
                    if (_keepFiles && file.isDirectory())
                        LOGGER.info("keeping " + file.getAbsolutePath() + " directory");
                    else if (file.isDirectory()){
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException e) {
                            LOGGER.error("cannot delete directory",e);
                        }
                    }
                    else {
                        if (!file.getName().matches(".*(\\.xml|\\.json|\\.tbx)")){
                            file.delete();
                        }
                    }
                }
        );
    }
}
