package org.atilf.monitor.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by smeoni on 23/03/17.
 */
public class TermithProgressTimer extends TimerTask {

    private final Collection _collection;
    private int _done;
    private ScheduledExecutorService _service = Executors.newSingleThreadScheduledExecutor();
    private Logger _logger;
    private final ExecutorService _executorService;
    private long _delay = 0;
    private long _interval = 100;
    DecimalFormat df = new DecimalFormat("#.##");


    public TermithProgressTimer(Collection collection, int done, Class className, ExecutorService executorService) {
        _collection = collection;
        _done = done;
        _logger = LoggerFactory.getLogger(this.getClass().getName() + " - " + className.getSimpleName());
        _executorService = executorService;
        df.setRoundingMode(RoundingMode.CEILING);
    }



    public void start(){
        _service = Executors.newSingleThreadScheduledExecutor();
        _service.scheduleAtFixedRate(this, _delay, _interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (_executorService.isTerminated()) {
            _service.shutdownNow();
        }
        float absoluteProgression = ((float) _collection.size() / (float) _done) * 100;
        _logger.info("progress : " + _collection.size()+ "/" + _done + " [" + df.format(absoluteProgression)+"%]");
    }

}
