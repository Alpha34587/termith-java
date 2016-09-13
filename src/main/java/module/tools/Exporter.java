package module.tools;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.TermithText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to execute the different output result of the TermithText class
 * @see TermithText
 * @author Simon Meoni
 * Created on 16/08/16.
 */


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
                (key,value) -> executor.submit(new FileWriterWorker(key,value))
        );
        LOGGER.info("Waiting executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }

    class FileWriterWorker implements Runnable {

        String key;
        StringBuffer value;

        public FileWriterWorker(String key, StringBuffer value){
            this.key = key;
            this.value = value;
        }
        @Override
        public void run() {

            LOGGER.info("writing : " + termithIndex.getOutputPath() + "/" + key + ".xml");
            int startText = value.indexOf("<text>");
            int endText = value.indexOf("</TEI>");
            value.delete(startText,endText);
            value.insert(startText,termithIndex.getTokenizeTeiBody().get(key));
            try {
                BufferedWriter bufferedWriter =
                        Files.newBufferedWriter(Paths.get(termithIndex.getOutputPath() + "/" + key + ".xml"));

                bufferedWriter.write(String.valueOf(value));
                bufferedWriter.close();
            } catch (IOException e) {
                LOGGER.error("Some errors during files writing",e);
            }

        }
    }
}
