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

    private TermSuitePipeline _termsuitePipeline;

    /**
     * create a termsuite pipeline with decided parameter for the termith project
     * @param lang the specified language
     */
    public PipelineBuilder(String lang, String jsonPath,
                           String tbxTerminology,
                           String jsonTerminology,
                           String jarResource) {
        
        TermSuiteCLIUtils.setGlobalLogLevel(Level.INFO);
        _termsuitePipeline = TermSuitePipeline.create(lang)
                .setResourceJar(jarResource)
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
                .haeJsonExporter(jsonTerminology)
                .haeTbxExporter(tbxTerminology);
    }

    /**
     * run the termsuite pipeline
     */
    public void start(){
        this._termsuitePipeline.run();
    }

    public TermSuitePipeline get_termsuitePipeline() {
        return _termsuitePipeline;
    }

}
