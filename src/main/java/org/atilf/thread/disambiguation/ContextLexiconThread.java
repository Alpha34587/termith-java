package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.ContextExtractor;
import org.atilf.module.disambiguation.DisambiguationXslTransformer;
import org.atilf.module.disambiguation.CorpusLexiconExtractor;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * transform files of a corpus into working file format and extract context of the terminology entry of corpus
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class ContextLexiconThread extends Thread {

    private CountDownLatch _transformCounter;

    /**
     * this constructor initialize all the needed fields. The _transformCounter is a CountDownLatch object of
     * the java concurrent library. The _transformCounter is set to the number of the file of the corpus. At each
     * end of a transformation, the counter is decreased. When the counter is equals to 0, the context extraction phase
     * is executed.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     * @param poolSize the number of thread used during the process
     * @see CountDownLatch
     * @see TermithIndex
     */
    public ContextLexiconThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
        try {
            _transformCounter = new CountDownLatch(
                    (int) Files.list(TermithIndex.getLearningPath()).count()
            );
        } catch (IOException e) {
            _logger.error("could not find folder : ",e);
        }
    }

    public ContextLexiconThread(TermithIndex termithIndex) {
            this(termithIndex,Thread.DEFAULT_POOL_SIZE);
    }

    /**
     * this method is used to execute the different phase of the context extraction process
     * and the global corpus extraction process
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     * @see InterruptedException
     * @see DisambiguationXslTransformer
     * @see ContextExtractor
     * @see CorpusLexiconExtractor
     */

    @Override
    public void execute() throws IOException, InterruptedException {
        DisambiguationXslResources xslResources = new DisambiguationXslResources();

        /*
        Transformation phase
         */
        Files.list(TermithIndex.getLearningPath()).forEach(
                p -> _executorService.submit(new DisambiguationXslTransformer(
                        p.toFile(),
                        _transformCounter,
                        _termithIndex,
                        xslResources)
                )
        );

        /*
        wait until the xsl transformation is done for all the corpus files
         */
        _transformCounter.await();

        /*
        Extraction phase
         */
        _termithIndex.getLearningTransformedFile().values().forEach(
                (file) -> {
                    _executorService.submit(new ContextExtractor(file.toString(), _termithIndex.getContextLexicon()));
                    _executorService.submit(new CorpusLexiconExtractor(file.toString(), _termithIndex.getCorpusLexicon()));
                }
        );
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
