package org.atilf.delegate.disambiguation.txm;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.txm.TxmContextExtractor;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simon Meoni on 19/04/17.
 */
public class TxmContextExtractorDelegate extends Delegate{
    @Override
    public void executeTasks() throws IOException, InterruptedException {

        List<String> includeElement = new ArrayList<>();
        includeElement.add("p");
        includeElement.add("head");
        includeElement.add("cit");
        includeElement.add("note");
        includeElement.add("q");

        List<String> authorizedTag = new ArrayList<>();
        authorizedTag.add("NOM");
        authorizedTag.add("NAM");
        authorizedTag.add("ADV");
        authorizedTag.add("ADJ");
        authorizedTag.add("VER:pres");
        authorizedTag.add("VER:infi");
        authorizedTag.add("VER:pper");
        authorizedTag.add("VER:ppre");
        authorizedTag.add("VER:subp");
        authorizedTag.add("VER:cond");
        authorizedTag.add("VER:simp");
        authorizedTag.add("VER:futu");
        authorizedTag.add("VER:impf");
        authorizedTag.add("VER:subi");

        List<Future> futures = new ArrayList<>();
        Files.list(getFlowableVariable("out",null)).forEach(
                file -> futures.add(_executorService.submit(
                        new TxmContextExtractor(file.toString(),
                                _termithIndex.getTermsTxmContext(),
                                getFlowableVariable("window",0),
                                getFlowableVariable("annotation",""),
                                authorizedTag,
                                includeElement
                        ))
                )
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
