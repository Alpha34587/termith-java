package module.timer;

import models.TermithIndex;
import module.observer.ProgressBarObserver;
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
public class ProgressBarTimer extends TimerTask {


    private TermithIndex termithIndex;
    private Timer timer;
    private Logger logger;
    private long delay;
    private long interval;
    ScheduledExecutorService service;

    public ProgressBarTimer(TermithIndex termithIndex, Logger logger){
        this(termithIndex,logger,15);
    }

    public ProgressBarTimer(TermithIndex termithIndex, Logger logger, long interval) {
        this.termithIndex = termithIndex;
        this.timer = new Timer();
        this.logger = logger;
        this.delay = 0;
        this.interval = interval;
        termithIndex.getTermithObservable().addObserver(new ProgressBarObserver(),logger);
    }

    public void start(){
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, delay, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        // task to run goes here
        termithIndex.getTermithObservable().changeValue(termithIndex.getExtractedText().size(),
                termithIndex.getCorpusSize(), logger);
        if (termithIndex.getExtractedText().size() == termithIndex.getCorpusSize()){
            timer.cancel();
            service.shutdownNow();
        }

    }

}
