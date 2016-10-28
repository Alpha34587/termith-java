package org.atilf.worker;

import com.google.common.io.ByteStreams;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.termsuite.PipelineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        jsonCorpus = termithIndex._corpus + "/json";
    }

    @Override
    public void run() {
        LOGGER.info("Build Termsuite Pipeline");
        init();
        PipelineBuilder pipelineBuilder = new PipelineBuilder(
                TermithIndex.get_lang(),
                jsonCorpus,
                termithIndex.get_terminologies().get(0).toString(),
                termithIndex.get_terminologies().get(1).toString(),
                exportResource()
        );
        LOGGER.info("Run Termsuite Pipeline");
        pipelineBuilder.start();
        LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                termithIndex.get_corpus());

    }

    private void init() {
        
        termithIndex.get_terminologies().add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.tbx"));
        termithIndex.get_terminologies().add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.json"));
    }


    public String exportResource(){
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("termsuite-lang/termsuite-resources.jar");
        try {
            Files.write(Paths.get(termithIndex._corpus +"/termsuite-resources.jar"), ByteStreams.toByteArray(resourceAsStream));
        } catch (IOException e) {
            LOGGER.error("cannot copy termsuite-resources.jar",e);
        }
        return termithIndex._corpus +"/termsuite-resources.jar";
    }
}
