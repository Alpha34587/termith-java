package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.models.XslResources;
import org.atilf.module.extractor.TextExtractor;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.thread.InitializerThread;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TextExtractorWorker implements Runnable {

    private final XslResources xslResources;
    private Path path;
    private TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializerThread.class.getName());

    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path
     */
    public TextExtractorWorker(Path path, TermithIndex termithIndex) {
        this.path = path;
        this.termithIndex = termithIndex;
        this.xslResources = new XslResources();
    }

    /**
     * this override method run call the extractor text method
     */
    @Override
    public void run() {
        try {
            LOGGER.debug("Extracting text of file: " + this.path);
            TextExtractor textExtractor = new TextExtractor(path.toFile(),xslResources);
            StringBuilder extractedBuffer = textExtractor.xsltTransformation();
            if (extractedBuffer.length() != 0) {
                termithIndex.get_extractedText().put(FilesUtils.nameNormalizer(path.getFileName().toString()),
                        FilesUtils.writeObject(extractedBuffer,TermithIndex._outputPath));
            }
            else {
                LOGGER.info(this.path + " has empty body");
                termithIndex.set_corpusSize(termithIndex.get_corpusSize() - 1);
            }
            LOGGER.debug("Extraction done for file: " + this.path);
        } catch (IOException e) {
            LOGGER.error("File Exception",e);
        }
    }
}
