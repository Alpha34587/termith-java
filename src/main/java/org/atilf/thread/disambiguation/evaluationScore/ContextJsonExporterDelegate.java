package org.atilf.thread.disambiguation.evaluationScore;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguation.evaluationScore.ContextJsonExporter;
import org.atilf.thread.Delegate;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ContextJsonExporterDelegate extends Delegate {

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public ContextJsonExporterDelegate(TermithIndex termithIndex) throws IOException {
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
    public ContextJsonExporterDelegate(TermithIndex termithIndex, int poolSize) throws IOException {

        super(termithIndex, poolSize);
    }

    /**
     * this method is used to executeTasks the different steps of processing of a thread
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
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {
        _executorService.submit(new ContextJsonExporter(_termithIndex,TermithIndex.getScorePath())).get();
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
