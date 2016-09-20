package org.atilf.module.termsuite.json;

/**
 * Created by simon on 13/09/16.
 */

import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * This class is a file watcher service who retrieve the file generate by termsuite and send it to a
 * TeiMorphoSyntaxWorker task
 */
public class JsonTermsuiteObserver implements Observer {

    private final org.slf4j.Logger Logger = LoggerFactory.getLogger(JsonTermsuiteObserver.class.getName());

    @Override
    public void update(Observable observable, Object o) {

    }
}

