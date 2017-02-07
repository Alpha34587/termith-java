package org.atilf.monitor.timer;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

/**
 * @author Simon Meoni Created on 20/01/17.
 */
public class ProgressBarObserver implements Observer {

    private List<ProgressBarTimer> _progressBarTimers;
    private String _name;

    public ProgressBarObserver(String name, List<ProgressBarTimer> progressBarTimers) {
        _name = name;
        _progressBarTimers = progressBarTimers;
        _progressBarTimers.forEach(TimerTask::run);
    }



    @Override
    public void update(Observable observable, Object o) {

    }
    public String getName() {
        return _name;
    }
}
