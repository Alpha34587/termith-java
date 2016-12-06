package org.atilf.models.disambiguation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni Created on 06/12/16.
 */
public class CommonWordsPosLemmaCleaner implements Runnable {
    private CountDownLatch _commonCleanerCounter;
    private String _key;
    private LexiconProfile _lex1;
    private LexiconProfile _lex2;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonWordsPosLemmaCleaner.class.getName());

    public CommonWordsPosLemmaCleaner(String key, LexiconProfile lex1, LexiconProfile lex2,
                                      CountDownLatch commonCleanerCounter) {
        this(key, lex1, lex2);
        _commonCleanerCounter = commonCleanerCounter;
    }

    public CommonWordsPosLemmaCleaner(String key, LexiconProfile lex1, LexiconProfile lex2) {
        _key = key;
        _lex1 = lex1;
        _lex2 = lex2;
    }

    @Override
    public void run() {
        LOGGER.info("common pos/lemma cleaner started for : " + _key);
        execute();
        _commonCleanerCounter.countDown();
        LOGGER.info("common pos/lemma cleaner finished for : " + _key);
    }

    public void execute(){

    }
}
