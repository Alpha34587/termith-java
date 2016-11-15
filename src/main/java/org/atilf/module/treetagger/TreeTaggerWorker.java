package org.atilf.module.treetagger;

import org.atilf.models.termith.TermithIndex;
import org.atilf.models.termsuite.CorpusAnalyzer;
import org.atilf.models.termsuite.TextAnalyzer;
import org.atilf.models.treetagger.TagNormalizer;
import org.atilf.module.tei.morphology.SyntaxParserWrapper;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

/**
 * TreeTagger Wrapper calls two modules : TreeTaggerToJson and  SyntaxParserWrapper. The first module run the
 * morphology analysis and write termsuite morphology file and the second tokenize the xml file.
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeTaggerWorker.class.getName());
    private final String _id;
    private TermithIndex _termithIndex;
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
        _termithIndex = termithIndex;
        _jsonCnt = jsonCnt;
        _txt = FilesUtils.readObject(termithIndex.getExtractedText().get(id),StringBuilder.class);
        _jsonPath = termithIndex.getCorpus() + "/json/" + id + ".json";
        _textAnalyzer = corpusAnalyzer.getAnalyzedTexts().get(id);
        _xml = FilesUtils.readFile(termithIndex.getXmlCorpus().get(id));
        _id = id;

        try {
            /*
            delete file after reading the extracted text
             */
            Files.delete(termithIndex.getExtractedText().get(id));
        } catch (IOException e) {
            LOGGER.error("could not delete file",e);
        }
    }

    /**
     * the run method execute treeTaggerToJson module and SyntaxParserWrapper module
     */
    @Override
    public void run() {
        /*
        call init method : create json folder in the working directory.
         */
        init();
        /*
        instantiate TreeTaggerToJson object
         */
        TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson(
                _txt,
                _jsonPath,
                TermithIndex.getTreeTaggerHome(),
                TermithIndex.getLang(),
                _textAnalyzer,
                TermithIndex.getOutputPath().toString()
        );

        try {
            /*
            TreeTagger task and Json serialization
             */
            LOGGER.debug("TreeTagger task started for :" + _id);
            treeTaggerToJson.execute();
            _jsonCnt.countDown();
            _termithIndex.getSerializeJson().add(Paths.get(_jsonPath));
            LOGGER.debug("TreeTagger task finished for : " + _id);

            /*
            tokenize xml file
             */
            LOGGER.debug("tokenization and morphosyntax tasks started for : " + _jsonPath);
            File json = new File(_jsonPath);
            SyntaxParserWrapper syntaxParserWrapper = new SyntaxParserWrapper(json, _txt, _xml);
            syntaxParserWrapper.execute();

            /*
            retained tokenize body and json file in the termithIndex
             */
            _termithIndex.getTokenizeTeiBody().put(json.getName().replace(".json",""),
                    FilesUtils.writeObject(syntaxParserWrapper.getTokenizeBody(), _termithIndex.getCorpus()));

            _termithIndex.getMorphologyStandOff().put(json.getName().replace(".json",""),
                    FilesUtils.writeObject(syntaxParserWrapper.getOffsetId(), _termithIndex.getCorpus()));
            LOGGER.debug("tokenization and morphosyntax tasks finished file : " + _jsonPath);

        } catch (IOException e) {
            LOGGER.error("error during parsing TreeTagger data", e);
        } catch (InterruptedException e) {
            LOGGER.error("error during Tree Tagger Process : ",e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.error("error during xml tokenization parsing",e);
        }
    }

    /**
     * init method create json directory and initialize TagNormalizer according to language given by static field
     * on the TermithIndex class
     */
    private void init(){
        try {

            TagNormalizer.initTag(TermithIndex.getLang());
            Files.createDirectories(Paths.get(_termithIndex.getCorpus() + "/json"));
            LOGGER.debug("create temporary text files in " + _termithIndex.getCorpus() + "/json folder");
        } catch (IOException e) {
            LOGGER.error("cannot create directories : ",e);
        }
    }
}
