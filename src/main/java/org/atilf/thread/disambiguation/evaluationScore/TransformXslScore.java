package org.atilf.thread.disambiguation.evaluationScore;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.enrichment.XslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class TransformXslScore extends Thread{

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public TransformXslScore(TermithIndex termithIndex) throws IOException {
        this(termithIndex,DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     *
     * @see TermithIndex
     */
    public TransformXslScore(TermithIndex termithIndex, int poolSize) throws IOException {

        super(termithIndex, poolSize);
    }

    /**
     * this method is used to execute the different steps of processing of a thread
     *
     * @throws IOException
     *         thrown a IO exception if a file is not found or have a permission problem during the xsl transformation
     *         phase
     * @throws InterruptedException
     *         thrown if awaitTermination function is interrupted while waiting
     * @throws ExecutionException
     *         thrown a exception if a system process is interrupted
     */
    @Override
    public void execute() throws IOException, InterruptedException, ExecutionException {
        XslResources xslResources = new DisambiguationXslResources();
        _logger.info("transformation phase is started for TransformXslScore");
        Path scoreFolder = Files.createDirectory(Paths.get(TermithIndex.getOutputPath().toString() + "/score/"));
        Files.list(TermithIndex.getOutputPath())
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toString().endsWith(".xml"))
                .forEach(
                        p -> _executorService.submit(new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
                                _termithIndex.getTransformOutputDisambiguationFile(),
                                xslResources,
                                scoreFolder)));
;
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}

