package thread;

import module.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Initializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private ExecutorService executor;
    private Path base;
    private Map<String, StringBuffer> extractedText;
    private Map<String, StringBuffer> xmlCorpus;

    public Initializer(Path base) {
        this(DEFAULT_POOL_SIZE, base);
    }

    public Initializer(int poolSize, Path base) {
        this.base = base;
        executor = Executors.newFixedThreadPool(poolSize);
        extractedText = new ConcurrentHashMap<>();
        xmlCorpus = new ConcurrentHashMap<>();
    }

    public Map<String, StringBuffer> getExtractedText() {
        return extractedText;
    }

    public Map<String, StringBuffer> getXmlCorpus() { return xmlCorpus; }

    public void execute() throws IOException, InterruptedException {
        LOGGER.info("Starting initilization of files of folder: " + this.base);
        Files.list(base).forEach(p -> executor.submit(new InitialiazerWorker((Path) p)));
        LOGGER.info("Waiting executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }

    private class InitialiazerWorker implements Runnable {

        private Path path;

        InitialiazerWorker(Path path) {
            this.path = path;
        }

        @Override
        public void run() {
            try {
                LOGGER.info("Extracting text of file: " + this.path);
                TextExtractor textExtractor = new TextExtractor(path.toFile());
                StringBuffer buffer = textExtractor.xsltTransformation();
                extractedText.put(path.getFileName().toString().replace(".xml", ""), buffer);
                xmlCorpus.put(path.getFileName().toString().replace(".xml", ""), new StringBuffer(
                        String.join("\n",Files.readAllLines(path))
                ));
                LOGGER.info("Extraction done for file: " + this.path);
            } catch (IOException e) {
                LOGGER.info("File Exception",e);
            }
        }
    }
}
