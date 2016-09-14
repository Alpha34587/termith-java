package module.termsuite.json;

import eu.project.ttc.engines.cleaner.TermProperty;
import eu.project.ttc.engines.desc.TermSuiteCollection;
import eu.project.ttc.tools.TermSuitePipeline;
import eu.project.ttc.tools.cli.TermSuiteCLIUtils;

/**
 * @author Simon Meoni
 *         Created on 08/09/16.
 */
public class JsonPipelineBuilder {

    private TermSuitePipeline termsuitePipeline;

    /**
     * create a termsuite pipeline with decided parameter for the termith project
     * @param lang the specified language
     */
    public JsonPipelineBuilder(String lang, String jsonPath,
                           String tbxTerminology,
                           String jsonTerminology) {

        TermSuiteCLIUtils.setGlobalLogLevel("info");
        this.termsuitePipeline = TermSuitePipeline.create(lang)
                .setResourceFilePath(getClass().getResource("/termsuite-lang/termsuite-resources.jar").getPath())
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
