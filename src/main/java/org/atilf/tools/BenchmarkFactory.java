package org.atilf.tools;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.atilf.models.TermithIndex;
import org.atilf.monitor.observer.MemoryPerformanceEvent;
import org.atilf.monitor.observer.TermithEvent;
import org.atilf.monitor.observer.TimePerformanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Simon Meoni Created on 31/01/17.
 */
public class BenchmarkFactory {
    public static Path _performancePath = TermithIndex.getOutputPath();
    static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkFactory.class.getName());
    static final String TIME_HISTORY_JSON =  _performancePath + "/time_history.json";
    static final String MEMORY_HISTORY_JSON = _performancePath + "/memory_history.json";

    public static void export(List<? extends TermithEvent> termithEvents){
        Class<? extends TermithEvent> termithEventClass = termithEvents.get(0).getClass();
        if (termithEventClass == TimePerformanceEvent.class) {
            new TimeBenchmark(termithEvents).writeJson();
        }
        else if (termithEventClass == MemoryPerformanceEvent.class){
            new MemoryBenchmark(termithEvents).writeJson();

        }
    }

    static class TimeBenchmark extends Benchmark{

        private List<? extends TermithEvent> _termithEvents;

        TimeBenchmark(List<? extends TermithEvent> termithEvents) {
            _termithEvents = termithEvents;
        }

        void writeJson() {

            if (!Files.exists(Paths.get(TIME_HISTORY_JSON))) {
                initializeJson(_termithEvents,TimePerformanceEvent.class);
            }
            else {
                modifyJson(_termithEvents,TimePerformanceEvent.class);
            }

        }
    }

    static class MemoryBenchmark extends Benchmark{
        private final List<? extends TermithEvent> _termithEvents;

        MemoryBenchmark(List<? extends TermithEvent> termithEvents) {
            _termithEvents = termithEvents;
        }

        void writeJson() {
            if (!Files.exists(Paths.get(MEMORY_HISTORY_JSON))) {
                initializeJson(_termithEvents,MemoryPerformanceEvent.class);
            }
            else {
                modifyJson(_termithEvents,MemoryPerformanceEvent.class);
            }
        }
    }

    static class Benchmark {

        void modifyJson(List<? extends TermithEvent> termithEvents,
                        Class<? extends TermithEvent> termithClass) {
                File file = null;
                ObjectMapper mapper = new ObjectMapper();
                try {
                    if (termithClass == TimePerformanceEvent.class) {
                        file = new File(TIME_HISTORY_JSON);
                    }
                    else if (termithClass == MemoryPerformanceEvent.class) {
                        file = new File(MEMORY_HISTORY_JSON);
                    }

                    final JsonNode tree = mapper.readTree(file);
                    JsonNode node = tree.findValue("run");
                    modifyExistingJson(termithEvents, termithClass, mapper, (ArrayNode) node);
                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, tree);
                } catch (IOException e) {
                    LOGGER.error("cannot read json", e);
                }
        }

        private void modifyExistingJson(List<? extends TermithEvent> termithEvents,
                                        Class<? extends TermithEvent> termithClass, ObjectMapper mapper, ArrayNode node) {
            ObjectNode objectNode = mapper.createObjectNode();
            termithEvents.forEach(
                    el -> {
                        if (termithClass == TimePerformanceEvent.class) {
                            TimePerformanceEvent timePerformanceEvent = (TimePerformanceEvent) el;
                            objectNode.put(timePerformanceEvent.getName(), timePerformanceEvent.getElapsedTime());
                        }
                        else if (termithClass == MemoryPerformanceEvent.class) {
                            MemoryPerformanceEvent memoryPerformanceEvent = (MemoryPerformanceEvent) el;
                            objectNode.put(memoryPerformanceEvent.getName(), memoryPerformanceEvent.getUsedMemory());
                        }
                    }
            );
            node.add(objectNode);
        }

        void initializeJson(List<? extends TermithEvent> termithEvents,
                            Class<? extends TermithEvent> termithClass) {
            try {
                FileOutputStream fos = null;
                if (TimePerformanceEvent.class == termithClass) {
                    fos = new FileOutputStream(TIME_HISTORY_JSON);
                }
                else if (MemoryPerformanceEvent.class == termithClass) {
                    fos = new FileOutputStream(MEMORY_HISTORY_JSON);
                }

                writeTermithEvents(termithEvents, fos ,termithClass);

            } catch (FileNotFoundException e) {
                LOGGER.error("cannot find json file : ",e);
            } catch (IOException e) {
                LOGGER.error("cannot write json file : ",e);
            }
        }

        private void writeTermithEvents(List<? extends TermithEvent> termithEvents, FileOutputStream fos,
                                        Class<? extends TermithEvent> termithClass) throws IOException {
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            JsonFactory jFactory = new JsonFactory();
            JsonGenerator jg = jFactory.createGenerator(writer);
            jg.useDefaultPrettyPrinter();
            jg.writeStartObject();
            jg.writeArrayFieldStart("run");
            jg.writeStartObject();

            if (termithClass == TimePerformanceEvent.class) {
                termithEvents.forEach(el -> {
                    TimePerformanceEvent timePerformanceEvent = (TimePerformanceEvent) el;
                    try {
                        jg.writeNumberField(timePerformanceEvent.getName(), timePerformanceEvent.getElapsedTime());
                    } catch (IOException e) {
                        LOGGER.error("cannot write number value", e);
                    }
                });
            }

            else if (termithClass == MemoryPerformanceEvent.class) {
                termithEvents.forEach(el -> {
                    MemoryPerformanceEvent memoryPerformanceEvent = (MemoryPerformanceEvent) el;
                    try {
                        jg.writeNumberField(memoryPerformanceEvent.getName(),
                                memoryPerformanceEvent.getUsedMemory());
                    } catch (IOException e) {
                        LOGGER.error("cannot write number value", e);
                    }
                });
            }

            jg.writeEndObject();
            jg.writeEndArray();
            jg.writeEndObject();
            jg.flush();
            writer.close();
        }
    }
}


