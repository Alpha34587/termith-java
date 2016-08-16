package runner;

import module.tool.Exporter;
import module.tool.TeiImporter;
import pipeline.Initializer;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Termith {

    private ExecutorService executorService;
    private TeiImporter teiImporter;
    private boolean trace;
    private ConcurrentHashMap<String, Initializer> initializedFiles = new ConcurrentHashMap<>();
    private String outputPath;

    public Termith() {}

    public Termith(Builder builder){
        teiImporter = builder.teiImporter;
        trace = builder.trace;
        outputPath = builder.outputPath;
    }

    public ConcurrentHashMap<String, Initializer> getInitializedFiles() {
        return initializedFiles;
    }

    public void run() throws IOException, InterruptedException, TransformerException {

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        teiImporter.getCorpus().forEach((f) -> {
            try {
                File file = (File) f;
                initializedFiles.put(
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


        if (outputPath != null){
            Exporter.export(outputPath,trace,this);
        }

    }

    public static class Builder
    {
        TeiImporter teiImporter;
        boolean trace = false;
        String outputPath = null;

        public Builder teiImporter(String path) throws IOException {
            this.teiImporter = new TeiImporter(path);
            return this;
        }

        public Builder trace(boolean activate){
            this.trace = activate;
            return this;
        }

        public Builder export(String outputPath){
            this.outputPath = outputPath;
            return this;
        }

        public Termith build() {
            Termith termith =  new Termith(this);
            return termith;
        }
    }

    }
