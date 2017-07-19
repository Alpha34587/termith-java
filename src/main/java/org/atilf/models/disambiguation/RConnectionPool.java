package org.atilf.models.disambiguation;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

/**
 * @author Simon Meoni Created on 26/01/17.
 */
public class RConnectionPool {
    private List<RConnectionThread> _rConnectionThreads = new CopyOnWriteArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(RConnectionPool.class.getName());
    private Semaphore _semaphore;

    public RConnectionPool(int poolSize, RLexicon rLexicon) {
        for (int i = 0; i < poolSize; i++){
            _rConnectionThreads.add(new RConnectionThread(rLexicon));
        }
        _semaphore = new Semaphore(poolSize);
    }

    public RConnection getRConnection(Thread thread){
        LOGGER.info("waiting R server connection ...");
        _semaphore.acquireUninterruptibly();
        LOGGER.info("R server connection established");
        for (RConnectionThread rConnectionThread : _rConnectionThreads) {
            if (rConnectionThread._thread == null) {
                rConnectionThread._thread = thread;
                return rConnectionThread._rConnection;
            }
        }
        return null;
    }

    public synchronized void releaseThread(Thread thread) {
        _rConnectionThreads.forEach(
                rConnectionThread -> {
                    if (rConnectionThread._thread == thread) {
                        rConnectionThread._thread = null;
                    }
                }
        );
        _semaphore.release();
    }

    public void removeThread(Thread thread) {
        _rConnectionThreads.forEach(
                rConnectionThread -> {
                    if (rConnectionThread._thread == thread) {
                        rConnectionThread._thread = null;
                    }
                }
        );
    }

    class RConnectionThread {
        RConnection _rConnection;
        Thread _thread;
        RConnectionThread(RLexicon rLexicon){
            try {
                _rConnection = new RConnection();
                _rConnection.eval(RResources.getScript().toString());
                _rConnection.eval("sumCol <-" + rLexicon.getSize());
                _rConnection.eval("lexic <- import_csv(\"" + rLexicon.getCsvPath() + "\")");
            } catch (RserveException e) {
                LOGGER.error("cannot established connection with R server",e);
            }
        }
    }
}


