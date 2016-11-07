package org.atilf.module.termsuite;

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
 * @author Simon Meoni
 *         Created on 08/09/16.
 */
public class TermsuitePipelineBuilder implements Runnable{

    private String _jsonCorpus;
    private TermSuitePipeline _termsuitePipeline;
    private TermithIndex _termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(TermsuitePipelineBuilder.class.getName());

    public TermsuitePipelineBuilder(TermithIndex termithIndex){
        _termithIndex = termithIndex;
        _jsonCorpus = termithIndex.getCorpus() + "/json";
        init();

        TermSuiteCLIUtils.setGlobalLogLevel(Level.INFO);
        _termsuitePipeline = TermSuitePipeline.create(TermithIndex.getLang())
                .setResourceJar(exportResource())
                .setCollection(
                        TermSuiteCollection.JSON,
                        _jsonCorpus,
                        "UTF-8")
                .aeUrlFilter()
                .aeStemmer()
                .aeStopWordsFilter()
                .aeRegexSpotter()
                .aeSpecificityComputer()
                .aeCompostSplitter()
//                .aeFixedExpressionSpotter()
                .aeMaxSizeThresholdCleaner(TermProperty.FREQUENCY, 100000)
                .aeSyntacticVariantGatherer()
                .aeGraphicalVariantGatherer()
                .aeThresholdCleaner(TermProperty.FREQUENCY, 5)
                .aeThresholdCleaner(TermProperty.DOCUMENT_FREQUENCY, 2)
                .aeTopNCleaner(TermProperty.WR, 10000)
                .aeContextualizer(3, true)
                .setExportJsonWithContext(false)
                .setExportJsonWithOccurrences(true)
                .haeJsonExporter(termithIndex.getJsonTerminology().toString())
                .haeTbxExporter(termithIndex.getTerminologies().get(0).toString());

    }

    /**
     * run the termsuite pipeline
     */
    public void execute(){
        this._termsuitePipeline.run();
    }

    public TermSuitePipeline getTermsuitePipeline() {
        return _termsuitePipeline;
    }

    @Override
    public void run() {
        LOGGER.info("Build Termsuite Pipeline");
        init();
        LOGGER.info("Run Termsuite Pipeline");
        execute();
        LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                _termithIndex.getCorpus());

    }

    private void init() {

        _termithIndex.getTerminologies().add(Paths.get(_jsonCorpus.replace("json","") + "/" + "terminology.tbx"));
        _termithIndex.getTerminologies().add(Paths.get(_jsonCorpus.replace("json","") + "/" + "terminology.json"));
    }


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
