package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * transform files of a corpus into working file format and extract context of the terminology entry of corpus
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class ContextExtractorDelegate extends Delegate {

    @Override
    public void execute() throws IOException, InterruptedException {

        List<String> includeElement = new ArrayList<>();
        includeElement.add("p");
        includeElement.add("head");
        includeElement.add("cit");
        includeElement.add("note");
        includeElement.add("q");
        List<String> authorizedTag = new ArrayList<>();
        authorizedTag.add("ADJ");
        authorizedTag.add("VER");
        authorizedTag.add("NOM");
        authorizedTag.add("ADV");
        authorizedTag.add("NAM");
        _termithIndex.getLearningTransformedFile().values().forEach(
                (file) -> _executorService.submit(
                        new ContextExtractor(file.toString(),
                                _termithIndex.getContextLexicon(),
                                _termithIndex.getCorpusLexicon(),
                                getFlowableVariable("window",0),
                                authorizedTag
                        )
                )
        );
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
