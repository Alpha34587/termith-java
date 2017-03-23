package org.atilf.monitor.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by smeoni on 23/03/17.
 */
public class TermithProgressTimer extends TimerTask {

    private Collection _collection;
    private List<Future> _futures = new ArrayList<>();
    private int _done;
    private ScheduledExecutorService _service = Executors.newSingleThreadScheduledExecutor();
    private Logger _logger;
    private final ExecutorService _executorService;
    private long _delay = 0;
    private long _interval = 20000;
    DecimalFormat df = new DecimalFormat("#.##");


    public TermithProgressTimer(Collection collection, int done, Class className, ExecutorService executorService) {
        _collection = collection;
        _done = done;
        _logger = LoggerFactory.getLogger(this.getClass().getName() + " - " + className.getSimpleName());
        _executorService = executorService;
        df.setRoundingMode(RoundingMode.CEILING);
    }

    public TermithProgressTimer(List<Future> futures, Class className, ExecutorService executorService) {

        _futures = futures;
        _logger = LoggerFactory.getLogger(this.getClass().getName() + " - " + className.getSimpleName());
        _executorService = executorService;
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
        if (_futures.isEmpty()) {
            showProgress(_collection.size(), _done);
        }

        else {
            int progress = (int) _futures.stream().filter(future -> future.isDone()).count();
            showProgress(progress,_futures.size());
        }
    }

    private void showProgress(int progress, int done) {
        float absoluteProgression = ((float) progress / (float) done) * 100;
        _logger.info("progress : " + progress + "/" + done + " [" + df.format(absoluteProgression) + "%]");
    }

}
