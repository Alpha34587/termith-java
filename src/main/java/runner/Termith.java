package runner;

import tool.TeiImporter;
import pipeline.Initializer;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Termith {
    ExecutorService executorService;
    TeiImporter teiImporter;
    ConcurrentHashMap<String, StringBuffer> initializeFiles;

    public Termith(TeiImporter teiImporter) {
        this.teiImporter = teiImporter;
        initializeFiles  = new ConcurrentHashMap<>();
    }

    public void run() throws IOException, InterruptedException, TransformerException {

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        teiImporter.getCorpus().forEach((f) -> {
            try {
                File file = (File) f;
                initializeFiles.put(file.getName(),
                        executorService.submit(new Initializer(file)).get());
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
        while (!executorService.isTerminated()){}

    }

    }
