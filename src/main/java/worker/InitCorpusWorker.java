package worker;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.Initializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class InitCorpusWorker implements Runnable{
    private Path path;
    private TermithIndex termithIndex;
    private CountDownLatch corpusCnt;
    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class.getName());

    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path
     * @param corpusCnt
     */
    public InitCorpusWorker(Path path, TermithIndex termithIndex, CountDownLatch corpusCnt) {
        this.path = path;
        this.termithIndex = termithIndex;
        this.corpusCnt = corpusCnt;
    }

    /**
     * this override method run call the extractor text method
     */
    @Override
    public void run() {
        try {
            termithIndex.getXmlCorpus().put(path.getFileName().toString().replace(".xml", ""), new StringBuffer(
                    String.join("\n", Files.readAllLines(path))
            ));
            corpusCnt.countDown();
        } catch (IOException e) {
            LOGGER.info("File Exception",e);
        }
    }
}
