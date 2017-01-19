package org.atilf.module;

import org.atilf.models.termith.TermithIndex;
import org.atilf.observer.TermithObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni Created on 19/01/17.
 */
public class Module {
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithObserver _termithObserver;
    protected TermithIndex _termithIndex;

    public Module(){

    }
}
