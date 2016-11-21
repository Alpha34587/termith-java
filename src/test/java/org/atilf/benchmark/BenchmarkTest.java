package org.atilf.benchmark;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.Thread;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.atilf.thread.disambiguation.DisambiguationExporterThread;
import org.atilf.thread.disambiguation.EvaluationThread;
import org.atilf.thread.disambiguation.LexiconProfileThread;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * Created by simon on 18/11/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BenchmarkTest {

    private static List<TermithIndex> _termithIndex = new ArrayList<>();
    public ThreadMXBean _threadMXBean = ManagementFactory.getThreadMXBean();
    private static Map<String, Long> timeHashMap = new LinkedHashMap<>();

    public static final int DUPLICATE = 3;
    public static final String LEARNING = "/home/smeoni/Documents/desamb/res/learning-test";
    public static final String EVALUATION = "/home/smeoni/Documents/desamb/res/learning-test";
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkTest.class.getName());

    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        for (int i = 1; i < DUPLICATE + 1; i++){
            createTermithIndex(i);
        }
    }

    private static void createTermithIndex(int i) throws IOException {
        File root = _temporaryFolder.newFolder(String.valueOf(i));
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

    private static  void createFiles(int i, Path learning, Path p) {
            for (int j = 1; j < i+1; j++ ) {
                try {
                    Files.copy(
                            p,
                            Paths.get(learning + "/" + p.getFileName().toString().replace(".xml", "-" + j + ".xml")
                            )
                    );
                } catch (IOException e) {
                    LOGGER.error("cannot copy files : ", e);
                }
            }
        };

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
    public static void printBenchmark() {
        timeHashMap.forEach(
                (key, value) -> LOGGER.info(key.split("-")[1] + " : " + value + "ms" + " " +
                        "size : " + _termithIndex.get(Integer.parseInt(key.split("-")[0])).getCorpusSize())
        );
    }

    public <T extends Thread> void ThreadPerformance(Class<T> threadClass) throws NoSuchMethodException {
        for (TermithIndex t : _termithIndex) {
            long startTime = _threadMXBean.getThreadCpuTime(java.lang.Thread.currentThread().getId());
            try {
                threadClass.getConstructor(TermithIndex.class, int.class).newInstance(t, 8).execute();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                    e) {
                LOGGER.error("cannot execute thread " + threadClass + " : " + e);
            } catch (InterruptedException | ExecutionException | IOException e) {
                LOGGER.error("some errors during execution " + threadClass + " : " + e);
            }
            long finishTime = _threadMXBean.getThreadCpuTime(java.lang.Thread.currentThread().getId());
            timeHashMap.put(_termithIndex.indexOf(t) + "-" + threadClass.getSimpleName(),
                    TimeUnit.MILLISECONDS.convert(finishTime - startTime, TimeUnit.NANOSECONDS));
        }
    }
}
