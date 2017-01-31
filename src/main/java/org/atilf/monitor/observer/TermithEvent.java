package org.atilf.monitor.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni Created on 19/01/17.
 */
public abstract class TermithEvent {

    protected String _name;
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());

    public TermithEvent(String name) {
        _name = name;
    }


}
