package org.atilf.worker;

import com.google.common.io.ByteStreams;
import org.atilf.models.TermithIndex;
import org.atilf.module.termsuite.PipelineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.atilf.models.TermithIndex.lang;
import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TermsuiteWorker implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(TermsuiteWorker.class.getName());
    private TermithIndex termithIndex;
    private String jsonCorpus;

    public TermsuiteWorker(TermithIndex termithIndex) {
        this.termithIndex = termithIndex;
        jsonCorpus = termithIndex.corpus + "/json";
    }

    @Override
    public void run() {
        LOGGER.info("Build Termsuite Pipeline");
        init();
        PipelineBuilder pipelineBuilder = new PipelineBuilder(
                lang,
                jsonCorpus,
                termithIndex.getTerminologies().get(0).toString(),
                termithIndex.getTerminologies().get(1).toString(),
                exportResource()
        );
        LOGGER.info("Run Termsuite Pipeline");
        pipelineBuilder.start();
        LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                termithIndex.getCorpus());

    }

    private void init() {
        
        termithIndex.getTerminologies().add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.tbx"));
        termithIndex.getTerminologies().add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.json"));
    }


    public String exportResource(){
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("termsuite-lang/termsuite-resources.jar");
        try {
            Files.write(Paths.get(termithIndex.corpus+"/termsuite-resources.jar"), ByteStreams.toByteArray(resourceAsStream));
        } catch (IOException e) {
            LOGGER.error("cannot copy termsuite-resources.jar",e);
        }
        return termithIndex.corpus+"/termsuite-resources.jar";
    }
}
