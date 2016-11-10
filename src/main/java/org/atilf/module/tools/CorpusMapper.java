package org.atilf.module.tools;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.enrichment.InitializerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class CorpusMapper implements Runnable {
    private Path path;
    private TermithIndex termithIndex;
    private CountDownLatch corpusCnt;
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializerThread.class.getName());

    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path path of the input file
     */
    public CorpusMapper(Path path, TermithIndex termithIndex) {
        this(path, termithIndex, new CountDownLatch(termithIndex.getXmlCorpus().size()));
    }

    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path path of the input
     * @param corpusCnt remains files to add to corpus
     */
    public CorpusMapper(Path path, TermithIndex termithIndex, CountDownLatch corpusCnt) {
        this.path = path;
        this.termithIndex = termithIndex;
        this.corpusCnt = corpusCnt;
    }

    public CountDownLatch getCorpusCnt() {
        return corpusCnt;
    }

    /**
     * this override method run call the extractor text method
     */
    @Override
    public void run() {
            termithIndex.getXmlCorpus().put(FilesUtils.nameNormalizer(path.getFileName().toString()), path);
            corpusCnt.countDown();
    }
}
