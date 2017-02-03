package org.atilf.module.enrichment.analyze;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.CorpusAnalyzer;
import org.atilf.models.enrichment.TagNormalizer;
import org.atilf.models.enrichment.TextAnalyzer;
import org.atilf.models.enrichment.TreeTaggerParameter;
import org.atilf.module.Module;
import org.atilf.module.enrichment.analyze.treeTaggerWorker.MorphologySerializer;
import org.atilf.module.enrichment.analyze.treeTaggerWorker.MorphologyTokenizer;
import org.atilf.module.enrichment.analyze.treeTaggerWorker.TreeTaggerWrapper;
import org.atilf.tools.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

/**
 * TreeTagger Wrapper calls two modules : TreeTaggerToJson and  MorphologyTokenizerWrapper. The first module run the
 * morphology analysis and write termsuite morphology file and the second tokenize the xml file.
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker extends Module {

    private final String _id;
    private CountDownLatch _jsonCnt;
    private StringBuilder _txt;
    private String _jsonPath;
    private StringBuilder _xml;
    private TextAnalyzer _textAnalyzer;

    /**
     *
     * @param termithIndex the termithIndex of the associated Thread
     * @param corpusAnalyzer this object contains the metadata used for write json file
     * @param id the name of the file in the map who contains the extracted text of the xml file
     * @param jsonCnt this counter is decreased when the json file is wrote
     */
    public TreeTaggerWorker(TermithIndex termithIndex, CorpusAnalyzer corpusAnalyzer, String id,
                            CountDownLatch jsonCnt) {
        super(termithIndex);
        _jsonCnt = jsonCnt;
        _txt = FilesUtils.readObject(termithIndex.getExtractedText().get(id),StringBuilder.class);
        _jsonPath = TermithIndex.getOutputPath() + "/json/" + id + ".json";
        _textAnalyzer = corpusAnalyzer.getAnalyzedTexts().get(id);
        _xml = FilesUtils.readFile(termithIndex.getXmlCorpus().get(id));
        _id = id;

        try {
            /*
            delete file after reading the extracted text
             */
            Files.delete(termithIndex.getExtractedText().get(id));
        } catch (IOException e) {
            _logger.error("could not delete file",e);
        }
    }

    /**
     * the run method execute treeTaggerToJson module and MorphologyTokenizerWrapper module
     */
    @Override
    public void execute() {
        /*
        call init method : create json folder in the working directory.
         */
        init();
        /*
        instantiate TreeTaggerToJson object
         */
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(
                _txt,
                new TreeTaggerParameter(false, TermithIndex.getLang(), TermithIndex.getTreeTaggerHome()),
                TermithIndex.getOutputPath().toString()
        );

        try {
            /*
            TreeTagger task and Json serialization
             */
            _logger.debug("TreeTagger task started for :" + _id);
            treeTaggerWrapper.execute();

            MorphologySerializer morphologySerializer = new MorphologySerializer(
                    treeTaggerWrapper.getTtOut(),
                    _jsonPath,
                    _txt,
                    _textAnalyzer);
            morphologySerializer.execute();
            _termithIndex.getSerializeJson().add(Paths.get(_jsonPath));
            _logger.debug("TreeTagger task finished for : " + _id);

            /*
            tokenize xml file
             */
            _logger.debug("tokenization and morphosyntax tasks started for : " + _jsonPath);
            File json = new File(_jsonPath);
            MorphologyTokenizer morphologyTokenizer = new MorphologyTokenizer(_txt, _xml, json);
            morphologyTokenizer.execute();

            /*
            retained tokenize body and json file in the termithIndex
             */
            _termithIndex.getTokenizeTeiBody().put(json.getName().replace(".json",""),
                    FilesUtils.writeObject(morphologyTokenizer.getTokenizeBuffer(), TermithIndex.getOutputPath()));

            _termithIndex.getMorphologyStandOff().put(json.getName().replace(".json",""),
                    FilesUtils.writeObject(morphologyTokenizer.getOffsetId(), TermithIndex.getOutputPath()));
            _logger.debug("tokenization and morphosyntax tasks finished file : " + _jsonPath);
            _jsonCnt.countDown();
        } catch (IOException e) {
            _logger.error("error during execute TreeTagger data", e);
        } catch (InterruptedException e) {
            _logger.error("error during Tree Tagger Process : ",e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            _logger.error("error during xml tokenization execute",e);
        }
    }

    /**
     * init method create json directory and initialize TagNormalizer according to language given by static field
     * on the TermithIndex class
     */
    private void init(){
        try {

            TagNormalizer.initTag(TermithIndex.getLang());
            Files.createDirectories(Paths.get(TermithIndex.getOutputPath() + "/json"));
            _logger.debug("create temporary text files in " + TermithIndex.getOutputPath() + "/json folder");
        } catch (IOException e) {
            _logger.error("cannot create directories : ",e);
        }
    }
}