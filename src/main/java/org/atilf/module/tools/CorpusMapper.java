package org.atilf.module.tools;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.Module;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class CorpusMapper extends Module {
    private Path path;
    private TermithIndex termithIndex;
    private CountDownLatch corpusCnt;
    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path path of the input file
     */
    public CorpusMapper(Path path, TermithIndex termithIndex,CountDownLatch corpusCnt) {
        super(termithIndex);
        this.path = path;
        this.termithIndex = termithIndex;
        this.corpusCnt = corpusCnt;
    }

    /**
     * put xml path on xmlCorpus hashmap
     */
    public void execute(){
        termithIndex.getXmlCorpus().put(FilesUtils.nameNormalizer(path.getFileName().toString()), path);
        corpusCnt.countDown();
    }
}
