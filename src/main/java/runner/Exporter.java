package runner;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.ExporterThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;


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
        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Tei exportation phase starting :");
        try{
            ExporterThread exporter = new ExporterThread(poolSize,termithIndex);
            exporter.execute();
        } catch ( Exception e ) {
            LOGGER.error("Error during execution of the extraction text phase : ",e);
            exit(1);
        }
    }
}
