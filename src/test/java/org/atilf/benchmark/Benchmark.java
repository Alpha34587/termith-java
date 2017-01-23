package org.atilf.benchmark;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.DisambiguationXslTransformer;
import org.atilf.module.tools.FilesUtils;
import org.atilf.thread.Thread;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.atilf.thread.disambiguation.DisambiguationExporterThread;
import org.atilf.thread.disambiguation.EvaluationThread;
import org.atilf.thread.disambiguation.LexiconProfileThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


/**
 * @author Simon Meoni
 * Created on 18/11/16.
 */
@JsonRootName("Iteration")
@SuppressWarnings("WeakerAccess")
public class Benchmark {
    public Integer _size = 0;
    public Long _dataContextExtractor = 0L;
    public Long _dataDisambiguation = 0L;
    public Long _dataR = 0L;
    public Long _dataEvaluationExtractor = 0L;
    public Long _dataExporter = 0L;
    private ObjectMapper _mapper = new ObjectMapper();
    private TermithIndex _termithIndex;

    private String _learning;
    private String _evaluation;
    private String _out;

    private static final String GRAPH_RESOURCE = "src/test/resources/benchmark/";
    private static final Logger LOGGER = LoggerFactory.getLogger(Benchmark.class.getName());

    public void setUp() throws IOException {
        createTermithIndex();
        countTerms();
    }

    public Benchmark(String learning, String evaluation, String out) {
        _learning = learning;
        _evaluation = evaluation;
        _out = out;
    }

    private void countTerms() throws IOException {
        Files.list(TermithIndex.getLearningPath()).forEach(
                file -> {
                    DisambiguationXslTransformer transformer = new DisambiguationXslTransformer(
                            file.toFile(),new DisambiguationXslResources()
                    );
                    try {
                        transformer.execute();
                        Path path = FilesUtils.writeFile(transformer.getTransformedContent(),
                                Paths.get(_out), file.getFileName().toString());
                        TermExtractor termExtractor = new TermExtractor(path.toString());
                        _size += termExtractor.countTerms();
                    } catch (IOException e) {
                        LOGGER.error("cannot transform file ", e);
                    }
                    LOGGER.info("counting done for file :" + file);

                }
        );
    }

    private void writeJson() throws IOException {
        List<Benchmark> benchmarkList = new ArrayList<>();
        if (Files.exists(Paths.get("history.json"))) {
            JavaType type = _mapper.getTypeFactory().
                    constructCollectionType(List.class, Benchmark.class);
            benchmarkList = _mapper.readValue(new File("history.json"), type);
        }
        benchmarkList.add(this);
        _mapper.writerWithDefaultPrettyPrinter().writeValue(new File("history.json"), benchmarkList);

        plotViewer();
    }

    /**
     * parsing javascript graph
     * @throws IOException thrown an exception if files cannot found
     */
    private void plotViewer() throws IOException {
        List<Integer> sizeList = new ArrayList<>();
        JavaType type = _mapper.getTypeFactory().
                constructCollectionType(List.class, LinkedHashMap.class);
        List<LinkedHashMap> benchmarkList = _mapper.readValue(new File("history.json"),type);
        for (LinkedHashMap benchmarkTest : benchmarkList) {
            sizeList.add((Integer) benchmarkTest.get("_size"));
        }
        String jsDataVariable = "labels: " + sizeList.toString() + ",\n";

        jsDataVariable += "datasets : [ \n" +
                drawGraph("_dataContextExtractor",benchmarkList, "Context & Corpus Lexicon Extractor") +
                ",\n" +
                drawGraph("_dataDisambiguation",benchmarkList, "Disambiguation") +
                ",\n" +
                drawGraph("_dataR",benchmarkList, "R coefficient") +
                ",\n" +
                drawGraph("_dataEvaluationExtractor",benchmarkList, "Context Evaluation Extractor") +
                ",\n" +
                drawGraph("_dataExporter",benchmarkList, "Exporter") +
                "\n]";

        String js = String.join("\n", Files.readAllLines(Paths.get("src/test/resources/benchmark/graph.js")));
        js = js.replace("{}",  "{\n"+ jsDataVariable +" }\n");

        writeResultFile(js);
    }

    private void writeResultFile(String js) throws IOException {
        Files.write(Paths.get(_out + "/graph.js"),js.getBytes());
        Files.copy(
                Paths.get(GRAPH_RESOURCE + "/Chart.js"),
                Paths.get(_out + "/Chart.js"),REPLACE_EXISTING
        );
        Files.copy(
                Paths.get(GRAPH_RESOURCE + "/graph.html"),
                Paths.get(_out + "/graph.html"),REPLACE_EXISTING
        );
    }

    private String drawGraph(String jsonEntry, List<LinkedHashMap> benchmarkList, String name) {
        Color color = generateRandomColor();
        List<Integer> threadPerformance = new ArrayList<>();
        for (LinkedHashMap benchmarkTest : benchmarkList) {
            threadPerformance.add((Integer) benchmarkTest.get(jsonEntry));
        }

        return
                "{"+
                        "label : \"" + name +"\","+
                        "data:" + threadPerformance.toString() + "," +
                        "fill: false," +
                        "pointBorderColor: \"black\"," +
                        "pointBackgroundColor: \"black\"," +
                        "backgroundColor: \"rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ",0.6)\","+
                        "borderColor: \"rgba(" + color.getRed() + "," + color.getGreen()+ "," +color.getBlue() +",0.6)\","+
                        "pointBorderWidth: 1,"+
                        "pointHoverRadius: 5,"+
                        "pointHoverBackgroundColor: \"black\","+
                        "pointHoverBorderColor: \"black\","+
                        "pointHoverBorderWidth: 2,"+
                        "pointRadius: 1," +
                        "}";
    }

    /**
     * execute Thread and calculate CPU time
     * @param <T> thread inherited class
     * @param threadClass the org.atilf.thread to execute
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

        long time = TimeUnit.SECONDS.convert(finishTime - startTime, TimeUnit.NANOSECONDS);
        _dataDisambiguation += time;
        return time;
    }

    /**
     * create several termithIndex associated to different corpus
     */
    private void createTermithIndex() throws IOException {
        _termithIndex = new TermithIndex.Builder()
                .learningFolder(_learning)
                .evaluationFolder(_evaluation)
                .export(_out)
                .build();
    }

    /**
     * execute each steps of benchmark
     * @throws IOException thrown an exception during benchmark if a file had write or permission problems
     * @throws NoSuchMethodException thrown an exception during the call of constructor with ThreadPerformance
     */
    public void execute() throws IOException, NoSuchMethodException {
        setUp();
        _dataContextExtractor = ThreadPerformance(ContextLexiconThread.class);
        _dataR = ThreadPerformance(LexiconProfileThread.class);
        _dataEvaluationExtractor = ThreadPerformance(EvaluationThread.class);
        _dataExporter = ThreadPerformance(DisambiguationExporterThread.class);
        writeJson();
    }

    public Color generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // mix the color
        return new Color(red, green, blue);
    }
}
