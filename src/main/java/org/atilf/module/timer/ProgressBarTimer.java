package org.atilf.module.timer;

import org.atilf.models.TermithIndex;
import org.atilf.module.observer.ProgressBarObserver;
import org.atilf.module.observer.TermithObservable;
import org.slf4j.Logger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
abstract class ProgressBarTimer extends TimerTask {

    protected Timer _timer = new Timer();
    protected TermithObservable _termithObservable = new TermithObservable();
    protected TermithIndex _termithIndex;
    protected Logger _logger;
    protected long _delay;
    protected long _interval;
    protected ScheduledExecutorService _service;

    public ProgressBarTimer(TermithIndex termithIndex, String message,Logger logger){
        this(termithIndex,logger,3000, message);
    }

    public ProgressBarTimer(TermithIndex termithIndex, Logger logger, long interval, String message) {
        _termithIndex = termithIndex;
        _logger = logger;
        _delay = 0;
        _interval = interval;
        _termithObservable.addObserver(new ProgressBarObserver(message),logger);
    }

    public void start(){
        _service = Executors.newSingleThreadScheduledExecutor();
        _service.scheduleAtFixedRate(this, _delay, _interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
    }

    protected void update(int done) {
        _termithObservable.changeValue(done,
                _termithIndex.get_corpusSize(), _logger);
        if (_termithIndex.get_corpusSize() == done){
            _timer.cancel();
            _service.shutdownNow();
        }
    }

}
