package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * transform files of a corpus into working file format and extract context of the terminology entry of corpus
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class ContextExtractorDelegate extends Delegate {

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
        _termithIndex.getLearningTransformedFile().values().forEach(
                (file) -> futures.add(_executorService.submit(
                        new ContextExtractor(file.toString(),
                                _termithIndex.getContextLexicon(),
                                _termithIndex.getCorpusLexicon(),
                                getFlowableVariable("window",0),
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
