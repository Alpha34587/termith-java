package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.TotalTermScore;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.AggregateTeiTerms;
import org.atilf.module.disambiguation.ComputeTermsScore;
import org.atilf.module.disambiguation.ComputeTotalTermsScore;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class EvaluationScoreThread extends Thread{
    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public EvaluationScoreThread(TermithIndex termithIndex) {
        super(termithIndex);
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
    public EvaluationScoreThread(TermithIndex termithIndex, int poolSize) {
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
        //transform tei file
        //p <=> tei transform file (_outputPath)
        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> _executorService.submit(new AggregateTeiTerms(p,value,_termithIndex.getScoreTerms()))
        );

        _termithIndex.getScoreTerms().forEach(
                (p,value) -> _executorService.submit(new ComputeTermsScore(p,value))
        );

        _executorService.submit(new ComputeTotalTermsScore(_termithIndex.getScoreTerms(),new TotalTermScore()));

        //exportToJson
        //exportToCsv
        //exportToGraphJs
    }
}
