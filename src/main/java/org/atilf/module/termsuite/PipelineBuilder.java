package org.atilf.module.termsuite;

import ch.qos.logback.classic.Level;
import eu.project.ttc.engines.cleaner.TermProperty;
import eu.project.ttc.engines.desc.TermSuiteCollection;
import eu.project.ttc.tools.TermSuitePipeline;
import eu.project.ttc.tools.cli.TermSuiteCLIUtils;

/**
 * @author Simon Meoni
 *         Created on 08/09/16.
 */
public class PipelineBuilder {

    private TermSuitePipeline termsuitePipeline;

    /**
     * create a termsuite pipeline with decided parameter for the termith project
     * @param lang the specified language
     */
    public PipelineBuilder(String lang, String jsonPath,
                           String tbxTerminology,
                           String jsonTerminology) {

        TermSuiteCLIUtils.setGlobalLogLevel(Level.INFO);
        this.termsuitePipeline = TermSuitePipeline.create(lang)
                .setResourceJar("src/main/resources/termsuite-lang/termsuite-resources.jar")
                .setCollection(
                        TermSuiteCollection.JSON,
                        jsonPath,
                        "UTF-8")
                .aeUrlFilter()
                .aeStemmer()
                .aeStopWordsFilter()
                .aeRegexSpotter()
                .aeSpecificityComputer()
                .aeCompostSplitter()
                .aeSyntacticVariantGatherer()
                .aeGraphicalVariantGatherer()
                .aeThresholdCleaner(TermProperty.DOCUMENT_FREQUENCY, 2)
                .aeThresholdCleaner(TermProperty.FREQUENCY, 5)
                .aeTermClassifier(TermProperty.FREQUENCY)
                .aeTopNCleaner(TermProperty.WR, 10000)
                .aeContextualizer(3, true)
                .setExportJsonWithContext(false)
                .setExportJsonWithOccurrences(true)
                .haeJsonExporter(jsonTerminology.toString())
                .haeTbxExporter(tbxTerminology.toString());
    }

    /**
     * run the termsuite pipeline
     */
    public void start(){
        this.termsuitePipeline.run();
    }

    public TermSuitePipeline getTermsuitePipeline() {
        return termsuitePipeline;
    }

}
