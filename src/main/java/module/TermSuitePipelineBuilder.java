package module;

import eu.project.ttc.engines.cleaner.TermProperty;
import eu.project.ttc.engines.desc.TermSuiteCollection;
import eu.project.ttc.tools.TermSuitePipeline;
import eu.project.ttc.tools.cli.TermSuiteCLIUtils;

/**
 * Created by Simon Meoni on 16/08/16.
 */
public class TermSuitePipelineBuilder {
    private TermSuitePipeline termsuitePipeline;
    private String jsonPath;

    /**
     * create a termsuite pipeline with decided parameter for the termith project
     * @param lang the specified language
     * @param textPath the path of input text corpus
     * @param treeTaggerHome the path for Tagger
     */
    public TermSuitePipelineBuilder(String lang, String textPath, String treeTaggerHome) {

        TermSuiteCLIUtils.setGlobalLogLevel("info");
        this.jsonPath = textPath.replace("/txt","/json");
        this.termsuitePipeline = TermSuitePipeline.create(lang)
                .setResourceFilePath(getClass().getResource("/termsuite-lang/termsuite-resources.jar").getPath())
                .setCollection(
                        TermSuiteCollection.TXT,
                        textPath,
                        "UTF-8")
                .aeWordTokenizer()
                .setTreeTaggerHome(treeTaggerHome)
                .aeTreeTagger()
                .aeUrlFilter()
                .aeStemmer()
                .aeStopWordsFilter()
                .aeRegexSpotter()
                .aeSpecificityComputer()
                .aeCompostSplitter()
                .haeTermsuiteJsonCasExporter(jsonPath)
                .aeSyntacticVariantGatherer()
                .aeGraphicalVariantGatherer()
                .aeThresholdCleaner(TermProperty.DOCUMENT_FREQUENCY, 2)
                .aeThresholdCleaner(TermProperty.FREQUENCY, 5)
                .aeTermClassifier(TermProperty.FREQUENCY)
                .aeTopNCleaner(TermProperty.WR, 10000)
                .aeContextualizer(3, true)
                .setExportJsonWithContext(false)
                .setExportJsonWithOccurrences(true)
                .haeJsonExporter(textPath.replace("txt","") + "/" + "terminology.json")
                .haeTbxExporter(textPath.replace("txt","") + "/" + "terminology.tbx");
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
