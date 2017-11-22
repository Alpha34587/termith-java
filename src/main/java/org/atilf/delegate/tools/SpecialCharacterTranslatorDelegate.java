package org.atilf.delegate.tools;

import org.atilf.delegate.Delegate;
import org.atilf.module.tools.SpecialCharacterTranslator;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SpecialCharacterTranslatorDelegate extends Delegate {

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
    }

    /**
     * executeTasks the extraction text task with the help of inner InitializerWorker class
     *
     * @throws IOException          throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException {

        /*
        extract the text and map the path of the corpus into hashMap with identifier
         */
        _termithIndex.getXmlCorpus().forEach(
                (key, value) -> _executorService.submit(new SpecialCharacterTranslator(key,_termithIndex))
        );
        _logger.info("Waiting SpecialCharacterTranslator executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("SpecialCharacter tasks is finished");
    }
}