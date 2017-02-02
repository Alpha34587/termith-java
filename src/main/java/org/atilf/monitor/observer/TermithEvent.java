package org.atilf.monitor.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni Created on 19/01/17.
 */
public abstract class TermithEvent {

    String _name;
    int _corpusSize;
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass());

    TermithEvent(String name, int corpusSize) {
        _name = name;
        _corpusSize = corpusSize;
    }

    public String getName() {
        return _name;
    }

    public int getCorpusSize() {
        return _corpusSize;
    }
}
