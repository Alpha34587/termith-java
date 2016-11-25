package org.atilf.benchmark;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.Thread;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.atilf.thread.disambiguation.DisambiguationExporterThread;
import org.atilf.thread.disambiguation.EvaluationThread;
import org.atilf.thread.disambiguation.LexiconProfileThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


/**
 * Created by simon on 18/11/16.
 */
@JsonRootName("Iteration")
public class Benchmark {
    public Integer _sizes = 0;
    public Long _dataContext = 0L;
    public Long _dataDisambiguation = 0L;
    public Long _dataR = 0L;
    private ObjectMapper _mapper = new ObjectMapper();
    private TermithIndex _termithIndex;

    private static String _learning;
    private static String _evaluation;
    private static String _out;
    private static File _temporaryFolder;
    private static int _duplicate;
    private static final String GRAPH_RESOURCE = "src/test/resources/benchmark/";
    private static final Logger LOGGER = LoggerFactory.getLogger(Benchmark.class.getName());

    public void setUp() throws IOException {
        if(!_temporaryFolder.mkdir()){
            LOGGER.error("the temporary folder could has not been created");
        }
        createTermithIndex(_duplicate);
        warmUp(1000000);
    }

    private void warmUp(int count) {
        int i = 0;
        while (i < count) {i++;}
        LOGGER.info("warm up finished");
    }

    private void contextLexiconThread() throws NoSuchMethodException {
        _dataContext = ThreadPerformance(ContextLexiconThread.class);
        _dataDisambiguation += _dataContext;
    }
    private void lexiconProfileThread() throws NoSuchMethodException {
        _dataR = ThreadPerformance(LexiconProfileThread.class);
        _dataDisambiguation += _dataR;
    }

    private void evaluationThread() throws NoSuchMethodException {
        _dataDisambiguation += ThreadPerformance(EvaluationThread.class);
    }

    private void disambiguationExporter() throws NoSuchMethodException {
        _dataDisambiguation += ThreadPerformance(DisambiguationExporterThread.class);
    }

    private void writeJson() throws IOException {
        _sizes = _termithIndex.getCorpusSize();
        List<Benchmark> benchmarkList = new ArrayList<>();
        if (Files.exists(Paths.get("history.json"))) {
            JavaType type = _mapper.getTypeFactory().
                    constructCollectionType(List.class, Benchmark.class);
            benchmarkList = _mapper.readValue(new File("history.json"), type);
        }
        benchmarkList.add(this);
        _mapper.writerWithDefaultPrettyPrinter().writeValue(new File("history.json"), benchmarkList);

        plotViewer();
        FileUtils.deleteDirectory(_temporaryFolder);
    }

    /**
     * parsing javascript graph
     * @throws IOException thrown an exception if files cannot found
     */
    private void plotViewer() throws IOException {
        List<Integer> sizeList = new ArrayList<>();
        List<Integer> contextList = new ArrayList<>();
        List<Integer> disambiguationList = new ArrayList<>();
        List<Integer> rList = new ArrayList<>();

        JavaType type = _mapper.getTypeFactory().
                constructCollectionType(List.class, LinkedHashMap.class);
        List<LinkedHashMap> benchmarkList = _mapper.readValue(new File("history.json"),type);

        for (LinkedHashMap benchmarkTest : benchmarkList){
            sizeList.add((Integer) benchmarkTest.get("_sizes"));
            contextList.add((Integer) benchmarkTest.get("_dataContext"));
            disambiguationList.add((Integer) benchmarkTest.get("_dataDisambiguation"));
            rList.add((Integer) benchmarkTest.get("_dataR"));
        }
        String js = String.join("\n", Files.readAllLines(Paths.get("src/test/resources/benchmark/graph.js")));
        js = js.replace("labels: []", "labels: " + sizeList.toString());
        js = js.replaceFirst("data: \\[\\]", "data: " + disambiguationList.toString());
        js = js.replaceFirst("data: \\[\\]", "data: " + contextList.toString());
        js = js.replaceFirst("data: \\[\\]", "data: " + rList.toString());
        Files.write(Paths.get(_out + "graph.js"),js.getBytes());
        Files.copy(
                Paths.get(GRAPH_RESOURCE + "/Chart.js"),
                Paths.get(_out + "Chart.js"),REPLACE_EXISTING
        );
        Files.copy(
                Paths.get(GRAPH_RESOURCE + "/graph.html"),
                Paths.get(_out + "graph.html"),REPLACE_EXISTING
        );
    }

    /**
     * execute Thread and calculate CPU time
     * @param threadClass the org.atilf.thread to execute
     * @param <T> thread inherited class
     * @throws NoSuchMethodException throws an exception if constructor method cannot be called
     */
    private <T extends Thread> long ThreadPerformance(Class<T> threadClass) throws NoSuchMethodException {
        long startTime = System.nanoTime();
        try {
            threadClass.getConstructor(TermithIndex.class, int.class).newInstance(_termithIndex, 8).execute();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                e) {
            LOGGER.error("cannot execute thread " + threadClass + " : " + e);
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOGGER.error("some errors during execution " + threadClass + " : " + e);
        }
        long finishTime = System.nanoTime();

        return TimeUnit.MINUTES.convert(finishTime - startTime, TimeUnit.NANOSECONDS);
    }

    /**
     * create several termithIndex associated to different corpus
     * @param i number of TermithIndex object
     */
    private void createTermithIndex(int i) throws IOException {
        Path root = Files.createDirectories(
                Paths.get(_temporaryFolder.toString() + "/" + String.valueOf(i))
        );
        Path learning = Files.createDirectories(Paths.get(root.toString() + "/learning"));
        Path evaluation = Files.createDirectories(Paths.get(root.toString() + "/evaluation"));
        Path output = Files.createDirectories(Paths.get(root.toString() + "/out"));

        Files.list(Paths.get(_learning)).forEach(
                p -> createFiles(i, learning, p)
        );

        Files.list(Paths.get(Benchmark._evaluation)).forEach(
                p -> createFiles(i, evaluation, p)
        );

        _termithIndex = new TermithIndex.Builder()
                .learningFolder(learning.toString())
                .evaluationFolder(evaluation.toString())
                .export(output.toString())
                .build();

    }

    /**
     * Duplicate corpus
     * @param i number of files copies
     * @param corpus path of the duplicated corpus
     * @param p name of the corpus
     */
    private void createFiles(int i, Path corpus, Path p) {
        for (int j = 1; j < i+1; j++ ) {
            try {
                Files.copy(
                        p,
                        Paths.get(corpus + "/" + p.getFileName().toString().replace(".xml", "-" + j + ".xml")
                        )
                );
            } catch (IOException e) {
                LOGGER.error("cannot copy files : ", e);
            }
        }
    }

    /**
     * execute each steps of benchmark
     * @throws IOException thrown an exception during benchmar if a file had write or permission problems
     * @throws NoSuchMethodException thrown an exception during the call of constructor with ThreadPerformance
     */
    public void execute() throws IOException, NoSuchMethodException {
        this.setUp();
        this.contextLexiconThread();
        this.lexiconProfileThread();
        this.evaluationThread();
        this.disambiguationExporter();
        this.writeJson();
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException {
        _learning = args[0];
        _evaluation = args[1];
        _out = args[2];
        _temporaryFolder = new File(args[3]);
        _duplicate = Integer.parseInt(args[4]);
        Benchmark benchmark = new Benchmark();
        benchmark.execute();
    }
}
