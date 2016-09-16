package worker;

import models.TermithIndex;
import module.extractor.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.Initializer;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TextExtractorWorker implements Runnable {

    private Path path;
    private TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class.getName());

    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path
     */
    public TextExtractorWorker(Path path, TermithIndex termithIndex) {
        this.path = path;
        this.termithIndex = termithIndex;
    }

    /**
     * this override method run call the extractor text method
     */
    @Override
    public void run() {
        try {
            LOGGER.info("Extracting text of file: " + this.path);
            TextExtractor textExtractor = new TextExtractor(path.toFile());
            StringBuffer extractedBuffer = textExtractor.xsltTransformation();
            termithIndex.getExtractedText().put(path.getFileName().toString().replace(".xml", ""), extractedBuffer);
//            termithIndex.getXmlCorpus().put(path.getFileName().toString().replace(".xml", ""), new StringBuffer(
//                    String.join("\n", Files.readAllLines(path))
//            ));
            LOGGER.info("Extraction done for file: " + this.path);
        } catch (IOException e) {
            LOGGER.info("File Exception",e);
        }
    }
}
