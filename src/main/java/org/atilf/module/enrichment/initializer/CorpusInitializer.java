package org.atilf.module.enrichment.initializer;

import org.atilf.models.TermithIndex;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class CorpusInitializer extends Module {
    private Path _inputPath;
    private Path _outputPath;
    private TermithIndex termithIndex;
    /**
     * constructor of the class the parameter _inputPath is the _inputPath of the file that we want to treated
     * @param inputPath _inputPath of the input file
     */
    public CorpusInitializer(Path inputPath, Path outputPath,TermithIndex termithIndex) {
        super(termithIndex);
        _inputPath = inputPath;
        _outputPath = outputPath;
        this.termithIndex = termithIndex;
    }

    /**
     * put xml _inputPath on xmlCorpus hashMap
     */
    @Override
    public void execute(){
        Path outputFilePath = Paths.get(_outputPath + "/" + UUID.randomUUID().toString());
        try {
            Files.copy(_inputPath, outputFilePath);
        } catch (IOException e) {
            _logger.error("cannot copy file {}",_inputPath.toString(),e);
        }
        finally {
            termithIndex.getXmlCorpus().put(
                    FilesUtils.nameNormalizer(_inputPath.getFileName().toString()),
                    outputFilePath
            );
        }
    }
}
