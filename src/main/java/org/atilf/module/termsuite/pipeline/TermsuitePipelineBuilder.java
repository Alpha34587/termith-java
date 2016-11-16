package org.atilf.module.termsuite.pipeline;

import ch.qos.logback.classic.Level;
import com.google.common.io.ByteStreams;
import eu.project.ttc.engines.cleaner.TermProperty;
import eu.project.ttc.engines.desc.TermSuiteCollection;
import eu.project.ttc.tools.TermSuitePipeline;
import eu.project.ttc.tools.cli.TermSuiteCLIUtils;
import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * build a termsuite pipeline and run it.
 * @author Simon Meoni
 *         Created on 08/09/16.
 */
public class TermsuitePipelineBuilder implements Runnable{

    private String _jsonCorpus;
    private TermSuitePipeline _termsuitePipeline;
    private TermithIndex _termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(TermsuitePipelineBuilder.class.getName());

    /**
     * this constructor initialize the pipeline with all parameters needed for termsuite
     * @param termithIndex
     */
    public TermsuitePipelineBuilder(TermithIndex termithIndex){
        _termithIndex = termithIndex;
        _jsonCorpus = termithIndex.getCorpus() + "/json";
        /*
        create path for terminologies in termithIndex
         */
        init();
        TermSuiteCLIUtils.setGlobalLogLevel(Level.INFO);
        /*
         create termsuitePipeline
         */
        _termsuitePipeline = TermSuitePipeline.create(TermithIndex.getLang())
                /*
                 set Termsuite resource (it is a .jar version of termsuite resource)
                 */
                .setResourceJar(exportResource())
                /*
                set morphology json files folder
                 */
                .setCollection(
                        TermSuiteCollection.JSON,
                        _jsonCorpus,
                        "UTF-8")

                /*
                find term candidate
                 */
                .aeUrlFilter()
                .aeStemmer()
                .aeStopWordsFilter()
                .aeRegexSpotter()
                .aeSpecificityComputer()
                .aeCompostSplitter()
//                .aeFixedExpressionSpotter()
                /*
                limit the number of term candidate
                 */
                .aeMaxSizeThresholdCleaner(TermProperty.FREQUENCY, 100000)
                /*
                gathering some term candidate
                 */
                .aeSyntacticVariantGatherer()
                .aeGraphicalVariantGatherer()
                /*
                remove terms which have a frequency below a threshold
                 */
                .aeThresholdCleaner(TermProperty.FREQUENCY, 5)
                .aeThresholdCleaner(TermProperty.DOCUMENT_FREQUENCY, 2)
                .aeTopNCleaner(TermProperty.WR, 10000)
                .aeContextualizer(3, true)
                .setExportJsonWithContext(false)
                .setExportJsonWithOccurrences(true)
                /*
                export terminology
                 */
                .haeJsonExporter(termithIndex.getJsonTerminology().toString())
                .haeTbxExporter(termithIndex.getTerminologies().get(0).toString());

    }

    /**
     * run the termsuite pipeline
     */
    public void execute(){
        _termsuitePipeline.run();
    }

    /**
     *
     */
    @Override
    public void run() {
        LOGGER.info("Run Termsuite Pipeline");
        execute();
        LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                _termithIndex.getCorpus());

    }

    /**
     * add path terminology for termithIndex
     */
    private void init() {

        _termithIndex.getTerminologies().add(Paths.get(_jsonCorpus.replace("json","") + "/" + "terminology.tbx"));
        _termithIndex.getTerminologies().add(Paths.get(_jsonCorpus.replace("json","") + "/" + "terminology.json"));
    }


    /**
     * externalize termsuite jar resource
     * @return the path of termsuite jar resource
     */
    private String exportResource(){
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("termsuite-lang/termsuite-resources.jar");
        try {
            Files.write(Paths.get(_termithIndex.getCorpus() +"/termsuite-resources.jar"), ByteStreams.toByteArray(resourceAsStream));
        } catch (IOException e) {
            LOGGER.error("cannot copy termsuite-resources.jar",e);
        }
        return _termithIndex.getCorpus() +"/termsuite-resources.jar";
    }
}
