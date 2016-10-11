package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Simon Meoni
 *         Created on 11/10/16.
 */
public class Disambiguisation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());
    private static  final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor;
    private TermithIndex termithIndex;

    public Disambiguisation(TermithIndex termithIndex){
        this(termithIndex, POOL_SIZE);
    }

    public Disambiguisation(TermithIndex termithIndex, int poolSize){
        executor = Executors.newFixedThreadPool(poolSize);
        this.termithIndex = termithIndex;
    }

    public void execute() {

    }
}
