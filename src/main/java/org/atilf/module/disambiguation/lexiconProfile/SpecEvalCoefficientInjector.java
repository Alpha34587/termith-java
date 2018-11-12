package org.atilf.module.disambiguation.lexiconProfile;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.RConnectionPool;
import org.atilf.models.disambiguation.RLexicon;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.nio.file.Paths;
import java.util.UUID;

public class SpecEvalCoefficientInjector extends SpecCoefficientInjector {


    public SpecEvalCoefficientInjector(String id, String filename, TermithIndex termithIndex,
                                       RLexicon rLexicon, String outputPath, RConnectionPool rConnectionPool) {
        super();
        _termithIndex = termithIndex;
        _id = id;
        try {
            _rConnection = new RConnection();
        } catch (RserveException e) {
            _logger.error("cannot established connection with the R server",e);
        }
        _corpusLexicon = termithIndex.getCorpusLexicon();
        _lexiconProfile = termithIndex.getEvaluationLexicon().get(filename).get(id);
        _rLexicon = rLexicon;
        _rContextLexicon = new RLexicon(_lexiconProfile, _corpusLexicon,outputPath);
        _computeSpecificities = true;
        _rResultPath = Paths.get(outputPath + "/" + UUID.randomUUID().toString()).toAbsolutePath().toString();
        _rConnectionPool = rConnectionPool;

    }
}

