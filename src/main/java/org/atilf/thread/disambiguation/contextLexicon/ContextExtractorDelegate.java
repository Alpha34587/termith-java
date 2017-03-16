package org.atilf.thread.disambiguation.contextLexicon;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.thread.Delegate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * transform files of a corpus into working file format and extract context of the terminology entry of corpus
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class ContextExtractorDelegate extends Delegate {

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
    public ContextExtractorDelegate(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
    }

    public ContextExtractorDelegate(TermithIndex termithIndex) {
        this(termithIndex, DEFAULT_POOL_SIZE);
    }

    /**
     * this method is used to executeTasks the different phase of the context extraction process
     * and the global corpus extraction process
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     * @see InterruptedException
     * @see DisambiguationXslTransformer
     * @see ContextExtractor
     */

    @Override
    public void executeTasks() throws IOException, InterruptedException {

        List<String> includeElement = new ArrayList<>();
        includeElement.add("p");
        includeElement.add("head");
        includeElement.add("cit");
        includeElement.add("note");
        includeElement.add("q");
        List<String> authorizedTag = new ArrayList<>();
        authorizedTag.add("ADJ");
        authorizedTag.add("VER");
        authorizedTag.add("NOM");
        authorizedTag.add("ADV");
        authorizedTag.add("NAM");
        _termithIndex.getLearningTransformedFile().values().forEach(
                (file) -> _executorService.submit(
                        new ContextExtractor(file.toString(),
                                _termithIndex.getContextLexicon(),
                                _termithIndex.getCorpusLexicon(),
                                _termithIndex.getThresholdContext(),
                                authorizedTag
                        )
                )
        );
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
