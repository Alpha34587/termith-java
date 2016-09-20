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


    protected TermithIndex termithIndex;
    protected Timer timer;
    protected Logger logger;
    protected long delay;
    protected long interval;
    protected ScheduledExecutorService service;
    protected TermithObservable termithObservable;

    public ProgressBarTimer(TermithIndex termithIndex, String message,Logger logger){
        this(termithIndex,logger,15, message);
    }

    public ProgressBarTimer(TermithIndex termithIndex, Logger logger, long interval, String message) {
        this.termithIndex = termithIndex;
        this.timer = new Timer();
        this.logger = logger;
        this.delay = 0;
        this.interval = interval;
        this.termithObservable = new TermithObservable();
        termithObservable.addObserver(new ProgressBarObserver(message),logger);
    }

    public void start(){
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, delay, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
    }

    protected void update(int done) {
        termithObservable.changeValue(done,
                termithIndex.getCorpusSize(), logger);
        if (termithIndex.getCorpusSize() == done){
            timer.cancel();
            service.shutdownNow();
        }
    }

}
