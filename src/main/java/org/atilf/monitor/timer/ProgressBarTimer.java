package org.atilf.monitor.timer;

import org.atilf.models.termith.TermithIndex;
import org.atilf.monitor.observer.TermithObservable;
import org.slf4j.Logger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * abstract class for notify TermithObservable and add observer
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public abstract class ProgressBarTimer extends TimerTask {

    private Timer _timer = new Timer();
    private TermithObservable _termithObservable = new TermithObservable();
    protected TermithIndex _termithIndex;
    protected Logger _logger;
    private long _delay;
    private long _interval;
    private ScheduledExecutorService _service;

    /**
     * constructor for ProgressBarTimer
     * @param termithIndex termithIndex of a process
     * @param message the name of the task
     * @param logger the logger of the task
     */
    ProgressBarTimer(TermithIndex termithIndex, String message, Logger logger){
        this(termithIndex,logger,3000, message);
    }

    /**
     * constructor for ProgressBarTimer
     * @param termithIndex termithIndex of a process
     * @param message the name of the task
     * @param logger the logger of the task
     * @param interval the time interval between two notifications
     */
    ProgressBarTimer(TermithIndex termithIndex, Logger logger, long interval, String message) {
        _termithIndex = termithIndex;
        _logger = logger;
        _delay = 0;
        _interval = interval;
//        _termithObservable.addObserver(new ProgressBarObserver(message),logger);
    }

    /*
    begin the timer task
     */
    public void start(){
        _service = Executors.newSingleThreadScheduledExecutor();
        _service.scheduleAtFixedRate(this, _delay, _interval, TimeUnit.MILLISECONDS);
    }

    /**
     * update observer
     * @param done the number of task are done during process
     */
    protected void update(int done) {
//        _termithObservable.changeValue(done,
//                _termithIndex.getCorpusSize(), _logger);
//        if (_termithIndex.getCorpusSize() == done){
//            _timer.cancel();
//            _service.shutdownNow();
//        }
    }

}
