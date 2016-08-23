package thread;

import module.TextExtractor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Initializer {

    private static Logger LOGGER = Logger.getLogger(Initializer.class.getName());
    private static int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

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

    public void execute() throws Exception {
        LOGGER.log(Level.INFO, "Starting initilization of files of folder: " + this.base);
        Files.list(base).forEach((p) -> executor.submit(new InitialiazerWorker((Path) p)));
        LOGGER.log(Level.INFO, "Waiting executors to finish");
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
                LOGGER.log(Level.INFO, "Extracting text of file: " + this.path);
                TextExtractor textExtractor = new TextExtractor(path.toFile());
                StringBuffer buffer = textExtractor.XsltTransformation();
                extractedText.put(path.getFileName().toString().replace(".xml", ""), buffer);
                xmlCorpus.put(path.getFileName().toString().replace(".xml", ""), new StringBuffer(
                        String.join("\n",Files.readAllLines(path))
                ));
                LOGGER.log(Level.INFO, "Extraction done for file: " + this.path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
