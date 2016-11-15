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
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeTaggerWorker.class.getName());
    private final String _txtPath;
    private TermithIndex _termithIndex;
    private CountDownLatch _jsonCnt;
    private StringBuilder _txt;
    private String _jsonPath;
    private StringBuilder _xml;
    private TextAnalyzer _textAnalyzer;

    public TreeTaggerWorker(TermithIndex termithIndex, CorpusAnalyzer corpusAnalyzer, String id,
                            CountDownLatch jsonCnt) {
        _termithIndex = termithIndex;
        _jsonCnt = jsonCnt;
        _txt = FilesUtils.readObject(termithIndex.getExtractedText().get(id),StringBuilder.class);
        _txtPath = termithIndex.getCorpus() + "/txt/" + id + ".txt";
        _jsonPath = termithIndex.getCorpus() + "/json/" + id + ".json";
        _textAnalyzer = corpusAnalyzer.getAnalyzedTexts().get(id);
        _xml = FilesUtils.readFile(termithIndex.getXmlCorpus().get(id));

        try {
            Files.delete(termithIndex.getExtractedText().get(id));
        } catch (IOException e) {
            LOGGER.error("could not delete file",e);
        }
    }

    @Override
    public void run() {
        init();
        TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson(
                _txt,
                _jsonPath,
                TermithIndex.getTreeTaggerHome(),
                TermithIndex.getLang(),
                _textAnalyzer,
                TermithIndex.getOutputPath().toString()
        );


        try {
            LOGGER.debug("converting TreeTagger output started file : " + _txtPath);
            treeTaggerToJson.execute();
            _jsonCnt.countDown();
            _termithIndex.getSerializeJson().add(Paths.get(_jsonPath));
            LOGGER.debug("converting TreeTagger output finished file : " + _txtPath);

            LOGGER.debug("tokenization and morphosyntax tasks started file : " + _jsonPath);
            File json = new File(_jsonPath);
            SyntaxParserWrapper syntaxParserWrapper = new SyntaxParserWrapper(json, _txt, _xml);
            syntaxParserWrapper.execute();
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

    private void init(){
        try {
        TagNormalizer.initTag(TermithIndex.getLang());
        Files.createDirectories(Paths.get(_termithIndex.getCorpus() + "/txt"));
        LOGGER.debug("create temporary text files in " + _termithIndex.getCorpus() + "/txt folder");
            Files.createDirectories(Paths.get(_termithIndex.getCorpus() + "/json"));
        } catch (IOException e) {
            LOGGER.error("cannot create directories : ",e);
        }
    }

}
