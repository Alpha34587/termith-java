package org.atilf.benchmark;

import org.apache.commons.io.FileUtils;
import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.Thread;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.atilf.thread.disambiguation.DisambiguationExporterThread;
import org.atilf.thread.disambiguation.EvaluationThread;
import org.atilf.thread.disambiguation.LexiconProfileThread;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


/**
 * Created by simon on 18/11/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BenchmarkTest {

    private static List<TermithIndex> _termithIndex = new ArrayList<>();
    private static Map<String, Long> _timeHashMap = new LinkedHashMap<>();

    private static final int DUPLICATE = 1;
    private static final String LEARNING = "/home/smeoni/Documents/desamb/res/learning-test/";
    private static final String EVALUATION = "/home/smeoni/Documents/desamb/res/learning-test/";
    private static final String OUT = "/home/smeoni/Documents/test/";
    private static final String GRAPH_RESOURCE = "src/test/resources/benchmark/";
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkTest.class.getName());
    public static File _temporaryFolder = new File("testPerformance");

    @BeforeClass
    public static void setUp() throws IOException {
        _temporaryFolder.mkdir();
        for (int i = 1; i < DUPLICATE + 1; i++){
            createTermithIndex(i);
        }
//        warmUp(10);
    }

    public static void warmUp(int count) {
        for (int i = 0; i < count  ; i++){
            try {
                ContextLexiconThread.class.getConstructor(TermithIndex.class,int.class)
                        .newInstance(_termithIndex.get(0),8).execute();
            } catch (IOException | InterruptedException |
                    IllegalAccessException | InstantiationException |
                    NoSuchMethodException | InvocationTargetException e) {
                LOGGER.error("cannot execute Thread", e);
            }
        }
        LOGGER.info("warm up finished");
    }

    @Test
    public void test1ContextLexiconThread() throws NoSuchMethodException {
        ThreadPerformance(ContextLexiconThread.class);
    }
    @Test
    public void test2LexiconProfileThread() throws NoSuchMethodException {
        ThreadPerformance(LexiconProfileThread.class);
    }

    @Test
    public void test3EvaluationThread() throws NoSuchMethodException {
        ThreadPerformance(EvaluationThread.class);
    }

    @Test
    public void test4DisambiguationExporter() throws NoSuchMethodException {
        ThreadPerformance(DisambiguationExporterThread.class);
    }

    @AfterClass
    public static void printBenchmark() throws IOException {
        _timeHashMap.forEach(
                (key, value) -> LOGGER.info(key.split("-")[1] + " : " + value + "ms" + " " +
                        "size : " + _termithIndex.get(Integer.parseInt(key.split("-")[0])).getCorpusSize())
        );
        PlotViewer();
        FileUtils.deleteDirectory(_temporaryFolder);
    }

    /**
     * retrieve element used for draw graph
     * @throws IOException
     */
    private static void PlotViewer() throws IOException {
        List<Integer> sizes = new ArrayList<>();
        List<Long> dataContext = new ArrayList<>();
        Map<Integer,Long> dataDisambiguation = new HashMap<>();

        _timeHashMap.forEach(
                (key,value) ->
                {
                    int id = Integer.parseInt(key.split("-")[0]);
                    if(key.contains(ContextLexiconThread.class.getSimpleName())){
                        dataContext.add(value);
                        sizes.add(_termithIndex.get(id).getCorpusSize());
                    }

                    if (dataDisambiguation.containsKey(id)){
                        dataDisambiguation.put(id,dataDisambiguation.get(id) + value);
                    }
                    else {
                        dataDisambiguation.put(id,value);
                    }
                }
        );

        parseJsScript(sizes, dataContext, dataDisambiguation);
    }

    /**
     * parsing javascript graph
     * @param sizes size of the different corpus
     * @param dataContext the time elapsed during contexts threads
     * @param dataDisamb the time elapsed during all threads
     * @throws IOException
     */
    private static void parseJsScript(List<Integer> sizes, List<Long> dataContext, Map<Integer, Long> dataDisamb)
            throws IOException {
        String js = String.join("\n", Files.readAllLines(Paths.get("src/test/resources/benchmark/graph.js")));
        js = js.replace("labels: []", "labels: " + sizes.toString());
        js = js.replaceFirst("data: \\[\\]", "data: " + dataContext.toString());
        js = js.replaceFirst("data: \\[\\]", "data: " + dataDisamb.values().toString());
        Files.write(Paths.get(OUT + "graph.js"),js.getBytes());
        Files.copy(
                Paths.get(GRAPH_RESOURCE + "/Chart.js"),
                Paths.get(OUT + "Chart.js"),REPLACE_EXISTING
        );
        Files.copy(
                Paths.get(GRAPH_RESOURCE + "/graph.html"),
                Paths.get(OUT + "graph.html"),REPLACE_EXISTING
        );
    }

    /**
     * execute Thread and calculate CPU time
     * @param threadClass
     * @param <T>
     * @throws NoSuchMethodException
     */
    private <T extends Thread> void ThreadPerformance(Class<T> threadClass) throws NoSuchMethodException {
        for (TermithIndex t : _termithIndex) {
            long startTime = System.nanoTime();
            try {
                threadClass.getConstructor(TermithIndex.class, int.class).newInstance(t, 8).execute();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                    e) {
                LOGGER.error("cannot execute thread " + threadClass + " : " + e);
            } catch (InterruptedException | ExecutionException | IOException e) {
                LOGGER.error("some errors during execution " + threadClass + " : " + e);
            }
            long finishTime = System.nanoTime();
            _timeHashMap.put(_termithIndex.indexOf(t) + "-" + threadClass.getSimpleName(),
                    TimeUnit.SECONDS.convert(finishTime - startTime, TimeUnit.NANOSECONDS));
        }
    }

    /**
     * create several termithIndex associated to different corpus
     * @param i number of TermithIndex object
     */
    private static void createTermithIndex(int i) throws IOException {
        Path root = Files.createDirectories(
                Paths.get(_temporaryFolder.toString() + "/" + String.valueOf(i))
        );
        Path learning = Files.createDirectories(Paths.get(root.toString() + "/learning"));
        Path evaluation = Files.createDirectories(Paths.get(root.toString() + "/evaluation"));
        Path output = Files.createDirectories(Paths.get(root.toString() + "/out"));

        Files.list(Paths.get(LEARNING)).forEach(
                p -> createFiles(i, learning, p)
        );

        Files.list(Paths.get(EVALUATION)).forEach(
                p -> createFiles(i, evaluation, p)
        );

        _termithIndex.add(
                new TermithIndex.Builder()
                        .learningFolder(learning.toString())
                        .evaluationFolder(evaluation.toString())
                        .export(output.toString())
                        .build()

        );
    }

    /**
     * Duplicate corpus
     * @param i number of files copies
     * @param corpus path of the duplicated corpus
     * @param p name of the corpus
     */
    private static  void createFiles(int i, Path corpus, Path p) {
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
}
