package org.atilf.module.disambiguation.evaluation;

import org.atilf.models.disambiguation.LexiconProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni Created on 06/12/16.
 */
public class CommonWordsPosLemmaCleaner implements Runnable {
    private String _key;
    private LexiconProfile _lex1;
    private LexiconProfile _lex2;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonWordsPosLemmaCleaner.class.getName());

    public CommonWordsPosLemmaCleaner(String key, LexiconProfile lex1, LexiconProfile lex2) {
        this(lex1, lex2);
        _key = key;
    }

    public CommonWordsPosLemmaCleaner(LexiconProfile lex1, LexiconProfile lex2) {
        _lex1 = lex1;
        _lex2 = lex2;
    }

    @Override
    public void run() {
        LOGGER.debug("common pos/lemma cleaner started for : " + _key);
        execute();
        LOGGER.debug("common pos/lemma cleaner finished for : " + _key);
    }

    public void execute(){
        _lex2.getSpecCoefficientMap().keySet().forEach(
                key -> _lex1.getSpecCoefficientMap().keySet().remove(key)
        );
        _lex1.getSpecCoefficientMap().keySet().forEach(
                key -> _lex2.getSpecCoefficientMap().keySet().remove(key)
        );
    }
}
