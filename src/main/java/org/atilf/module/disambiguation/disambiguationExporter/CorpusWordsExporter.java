package org.atilf.module.disambiguation.disambiguationExporter;

import org.atilf.models.TermithIndex;
import org.atilf.module.Module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CorpusWordsExporter extends Module {

    private final String _outputPath;

    public CorpusWordsExporter(TermithIndex termithIndex, String outputPath) {
        _termithIndex = termithIndex;
        _outputPath = outputPath;
    }

    @Override
    protected void execute() {
        try {
            Files.write(
                    Paths.get(_outputPath + "/corpus"),
                    _termithIndex.getCorpusLexicon().getLexicalEntry().values()
            );
        } catch (IOException e) {
            _logger.error("cannot write corpus file");
        }
    }
}
