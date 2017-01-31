package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.atilf.thread.enrichment.AnalyzeThread;
import org.atilf.thread.enrichment.CleanerThread;
import org.atilf.thread.enrichment.ExporterThread;
import org.atilf.thread.enrichment.InitializerThread;
import org.atilf.tools.BenchmarkFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * this is the analysis process of morphology (with treeTagger) and terminology of a corpus
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TermithTreeTagger extends Runner {

    /**
     * this constructor initializes the _termithIndex field.
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public TermithTreeTagger(TermithIndex termithIndex) {
        super(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initializes the _termithIndex field and the number of thread used during the process
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     */
    public TermithTreeTagger(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
    }

    /**
     * The execute method of TermithTreeTagger has three phases :
     *      1) prepare the data : text extraction and keep the path of each corpus files
     *      2) analyze files : analyze the morphology of each files and transformed it on the termsuite format in order
     *      to analyze the terminology of the corpus and generate a terminology
     *      3) exporter : export the set of analysis into tei file format
     */
    @Override
    public void execute() throws IOException, InterruptedException {
        try {
        /*
        Extraction text phase
         */
            executeThread(InitializerThread.class,_termithIndex,_poolSize);
                    /*
        Morphology and terminology analysis phase
         */
            executeThread(AnalyzeThread.class,_termithIndex, _poolSize);
        /*
        Tei exportation phase
         */
            executeThread(ExporterThread.class,_termithIndex,_poolSize);
        /*
        Clean working directory
         */
            executeThread(CleanerThread.class,_termithIndex,_poolSize);

            BenchmarkFactory.export(_termithIndex.getMemoryPerformanceEvents());
            BenchmarkFactory.export(_termithIndex.getTimePerformanceEvents());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
