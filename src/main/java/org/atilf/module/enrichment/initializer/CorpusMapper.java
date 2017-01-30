package org.atilf.module.enrichment.initializer;

import org.atilf.models.TermithIndex;
import org.atilf.module.Module;
import org.atilf.tools.FilesUtils;

import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class CorpusMapper extends Module {
    private Path path;
    private TermithIndex termithIndex;
    /**
     * constructor of the class the parameter path is the path of the file that we want to treated
     * @param path path of the input file
     */
    public CorpusMapper(Path path, TermithIndex termithIndex) {
        super(termithIndex);
        this.path = path;
        this.termithIndex = termithIndex;
    }

    /**
     * put xml path on xmlCorpus hashmap
     */
    public void execute(){
        termithIndex.getXmlCorpus().put(FilesUtils.nameNormalizer(path.getFileName().toString()), path);
    }
}
