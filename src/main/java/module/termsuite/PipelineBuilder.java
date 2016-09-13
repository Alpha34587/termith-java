package module.termsuite;

import eu.project.ttc.engines.cleaner.TermProperty;
import eu.project.ttc.engines.desc.TermSuiteCollection;
import eu.project.ttc.engines.exporter.TermsuiteJsonCasExporter;
import eu.project.ttc.tools.TermSuitePipeline;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

/**
 * This class can build a termsuite pipeline adapted to the termith project
 * @author Simon Meoni
 * Created on 16/08/16.
 */
public class PipelineBuilder {

    private static JsonTermsuiteObserver termsuiteObserver = new JsonTermsuiteObserver();
    private static JsonTermsuiteObservable jsonTermsuiteObservable = new JsonTermsuiteObservable();
    private TermSuitePipeline termsuitePipeline;
    private String jsonPath;
    private Observer observer;

    /**
     * create a termsuite pipeline with decided parameter for the termith project
     * @param lang the specified language
     * @param textPath the path of input text corpus
     * @param treeTaggerHome the path for Tagger
     * @param jsonTermsuiteObserver
     */
    public PipelineBuilder(String lang, String textPath, String treeTaggerHome,
                           String tbxTerminology,
                           String jsonTerminology, JsonTermsuiteObserver jsonTermsuiteObserver) throws ResourceInitializationException {
        this.jsonPath = textPath.replace("/txt","/json");

        AnalysisEngineDescription ae = AnalysisEngineFactory.createEngineDescription(
                CustomTermsuiteJsonCasExporter.class,
                CustomTermsuiteJsonCasExporter.OUTPUT_DIRECTORY,
                this.jsonPath
        );

        jsonTermsuiteObservable.addObserver(jsonTermsuiteObserver);
        this.termsuitePipeline = TermSuitePipeline.create(lang)
                .setResourceFilePath(getClass().getResource("/termsuite-lang/termsuite-resources.jar").getPath())
                .setCollection(
                        TermSuiteCollection.TXT,
                        textPath,
                        "UTF-8")
                .setTreeTaggerHome(treeTaggerHome)
                .aeWordTokenizer()
                .aeTreeTagger()
                .customAE(ae, "custom Json ae")
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
                .haeJsonExporter(jsonTerminology)
                .haeTbxExporter(tbxTerminology);
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


    public static class CustomTermsuiteJsonCasExporter extends TermsuiteJsonCasExporter {

        @Override
        public void process(JCas aJCas) throws AnalysisEngineProcessException {
            super.process(aJCas);
            jsonTermsuiteObservable.update(Paths.get(this.directoryFile + "/" + this.getExportFilePath(aJCas, "json")));
        }
    }


    private static class JsonTermsuiteObservable extends Observable {
        public void update(Path path) {
            setChanged();
            notifyObservers(path);
        }
    }
}
