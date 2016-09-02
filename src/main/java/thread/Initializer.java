package thread;

import module.extractor.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the first phase of the termith process. The objective is to extract the text in order to send it to
 * a termsuite process.
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class Initializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private ExecutorService executor;
    private Path base;
    private Map<String, StringBuffer> extractedText;
    private Map<String, StringBuffer> xmlCorpus;

    public Initializer() {
        extractedText = new ConcurrentHashMap<>();
        xmlCorpus = new ConcurrentHashMap<>();
    }

    /**
     * a constructor of initialize who take on parameter a folder path with xml files
     * @param base the input folder
     */
    public Initializer(Path base) {
        this(DEFAULT_POOL_SIZE, base);
    }

    /**
     * this constructor can be specify the number of worker for this process
     * @param poolSize number of worker
     * @param base the input of folder
     */
    public Initializer(int poolSize, Path base) {
        this.base = base;
        executor = Executors.newFixedThreadPool(poolSize);
        extractedText = new ConcurrentHashMap<>();
        xmlCorpus = new ConcurrentHashMap<>();
    }

    /**
     * @return return the list of extracted text extracts by the process
     */
    public Map<String, StringBuffer> getExtractedText() {
        return extractedText;
    }


    /**
     * add a text manually to the corpus
     * @param id the name of the corpus
     * @param content the content of the corpus
     */
    public void addText(String id, StringBuffer content){extractedText.put(id,content);}


    /**
     * @return return the size of the total corpus
     */
    public int getTotalSize() {
        int totalOffset = -1;
        for (StringBuffer text : extractedText.values())
            totalOffset = totalOffset + text.length() + 1;

        return totalOffset;
    }

    /**
     * @return return the base corpus
     */
    public Map<String, StringBuffer> getXmlCorpus() { return xmlCorpus; }

    /**
     * execute the extraction text task with the help of inner InitializerWorker class
     * @throws IOException
     * @throws InterruptedException
     */
    public void execute() throws IOException, InterruptedException {
        LOGGER.info("Starting initilization of files of folder: " + this.base);
        Files.list(base).forEach(p -> executor.submit(new InitialiazerWorker((Path) p)));
        LOGGER.info("Waiting executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }

    /**
     * the initializerWorker have a run method who call a textExtractor object
     * @see TextExtractor
     */
    private class InitialiazerWorker implements Runnable {

        private Path path;

        /**
         * constructor of the class the parameter path is the path of the file that we want to treated
         * @param path
         */
        InitialiazerWorker(Path path) {
            this.path = path;
        }

        /**
         * this override method run call the extractor text method
         */
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
