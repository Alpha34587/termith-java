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
    boolean trace;
    ConcurrentHashMap<String, StringBuffer> initializeFiles = new ConcurrentHashMap<>();

    public Termith() {}

    public Termith(Builder builder){
        teiImporter = builder.teiImporter;
        trace = builder.trace;
    }

    public void Run() throws IOException, InterruptedException, TransformerException {

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        teiImporter.getCorpus().forEach((f) -> {
            try {
                File file = (File) f;
                initializeFiles.put(
                        file.getName(),
                        executorService.submit(new Initializer(file)).get()
                );
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
        while (!executorService.isTerminated()){}
        int a = 0;
    }

    public static class Builder
    {
        TeiImporter teiImporter;
        boolean trace = false;

        public Builder TeiImporter(String path) throws IOException {
            TeiImporter teiImporter = new TeiImporter(path);
            this.teiImporter = teiImporter;
            return this;
        }

        public Builder Trace(boolean activate){
            return this;
        }

        public Termith Build() {
            Termith termith =  new Termith(this);
            return termith;
        }
    }

    }
