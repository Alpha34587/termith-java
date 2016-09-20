package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.termsuite.PipelineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

import static org.atilf.models.TermithIndex.lang;

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
                termithIndex.getTerminologies().get(1).toString()
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
}
