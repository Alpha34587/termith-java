package runner;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.TeiWriterWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Exporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());
    private static  final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor;
    private TermithIndex termithIndex;

    public Exporter(TermithIndex termithIndex){
        this(termithIndex, POOL_SIZE);
    }

    public Exporter(TermithIndex termithIndex, int poolSize){
        executor = Executors.newFixedThreadPool(poolSize);
        this.termithIndex = termithIndex;
    }

    public void execute() throws InterruptedException {

        termithIndex.getXmlCorpus().forEach(
                (key,value) -> executor.submit(new TeiWriterWorker(key,value,termithIndex))
        );
        LOGGER.info("Waiting executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
