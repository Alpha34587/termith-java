package org.atilf.module.disambiguation.lexiconProfile;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.RConnectionPool;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.module.Module;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.atilf.models.disambiguation.AnnotationResources.LEX_OFF;
import static org.atilf.models.disambiguation.AnnotationResources.LEX_ON;

/**
 * compute specificity coefficient for each word of a term candidate entry. the result is retained on the
 * _lexicalProfile field. This class call a R script with the library Rcaller.
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoefficientInjector extends Module {
    private RConnection _rConnection;
    private RConnectionPool _rConnectionPool;
    private LexiconProfile _lexiconProfile;
    private RLexicon _rLexicon;
    private CorpusLexicon _corpusLexicon;
    private boolean _computeSpecificities = true;
    private RLexicon _rContextLexicon;
    private String _id;
    private String _rResultPath;

    /**
     *  constructor of SpecCoefficientInjector
     * @param lexiconProfile this is the lexiconProfile of the termithIndex
     * @param rLexicon the rLexicon variable is an object that contain the corpusLexicon converted into R variable and
     *                 the size of the corpus
     * @param corpusLexicon this the corpus of the termithIndex
     * @see LexiconProfile
     * @see RLexicon
     * @see CorpusLexicon
     */
    protected SpecCoefficientInjector(LexiconProfile lexiconProfile, RLexicon rLexicon, CorpusLexicon corpusLexicon,
                                      RConnection rConnection, String outputPath) {

        _corpusLexicon = corpusLexicon;
        _lexiconProfile = lexiconProfile;
        _rLexicon = rLexicon;
        _rConnection = rConnection;
        _rResultPath = outputPath + "/" + UUID.randomUUID().toString();
        /*
        instantiate _rContextLexicon
         */
        _rContextLexicon = new RLexicon(_lexiconProfile, _corpusLexicon,outputPath);
    }

    /**
     * constructor for SpecCoefficientInjector
     * @param id term entry id of key in lexicalProfile
     * @param termithIndex the termithIndex of a process
     * @param rLexicon the corpusLexicon converted into a RLexicon object that contains the R variable
     *                 of the corpusLexicon and his size
     */
    public SpecCoefficientInjector(String id, TermithIndex termithIndex, RLexicon rLexicon, String outputPath){
        super(termithIndex);
        _id = id;
        if (isLexiconPresent(termithIndex,id)) {
            try {
                _rConnection = new RConnection();
            } catch (RserveException e) {
                _logger.error("cannot established connection with the R server");
            }
            _corpusLexicon = termithIndex.getCorpusLexicon();
            _lexiconProfile = termithIndex.getContextLexicon().get(id);
            _rLexicon = rLexicon;
            _rContextLexicon = new RLexicon(_lexiconProfile, _corpusLexicon,outputPath);
            _computeSpecificities = true;
            _rResultPath = Paths.get(outputPath + "/" + UUID.randomUUID().toString()).toAbsolutePath().toString();
        }
        else {
            _computeSpecificities = false;
        }
    }

    public SpecCoefficientInjector(String id, TermithIndex termithIndex, RLexicon rLexicon, String outputPath,
                                   RConnectionPool rConnectionPool) {
        this(id,termithIndex,rLexicon,outputPath);
        _rConnectionPool = rConnectionPool;
    }

    private boolean isLexiconPresent(TermithIndex termithIndex, String id) {
        String on = id.split("_")[0] + LEX_ON.getValue();
        String off = id.split("_")[0] + LEX_OFF.getValue();
        return termithIndex.getContextLexicon().containsKey(on) && termithIndex.getContextLexicon().containsKey(off);
    }

    /**
     * call reduceToLexicalProfile method
     */
    public void execute() {
        _logger.debug("compute specificities coefficient for : " + _id);
        try {
            if (_computeSpecificities) {
                reduceToLexicalProfile(computeSpecCoefficient());
                try {
                    Files.delete(Paths.get(_rResultPath));
                    Files.delete(_rContextLexicon.getCsvPath());
                }
                catch (IOException e) {
                    _logger.error("cannot remove file : " + _rResultPath, e);
                }
                _logger.debug("specificities coefficient is computed for : " + _id);
            }
            else {
                _logger.debug("only terminology or non-terminology lexicon profile is present for : " +  _id);
            }
        }
        catch (Exception e){
            _logger.error("problem during the execution of SpecCoefficientInjector : ", e);
        }
    }


    /**
     * add specificityCoefficient calculated at each word in the _lexicalProfile
     * @param specificityCoefficient this float array contains specificities coefficients for all words
     *                               of a lexical profile
     */
    private void reduceToLexicalProfile(List<Float> specificityCoefficient) {
        int cnt = 0;
        for (String id : _rContextLexicon.getIdContextLexicon()) {
            _lexiconProfile.addCoefficientSpec(
                    _corpusLexicon.getLexicalEntry(Integer.parseInt(id)),
                    specificityCoefficient.get(cnt));
            cnt++;
        }
    }
    /**
     * this method calls the R script and compute specificity coefficient for each word of a context
     * @return coefficients specificities
     */
    List<Float> computeSpecCoefficient(){
        try {
            _rConnection.eval("tabCol <-" + "c(" + _rContextLexicon.getSize() + "," + _rLexicon.getSize() + ")");
            _rConnection.eval("names(tabCol) <- c(\"sublexicon\",\"complementary\")");
            _rConnection.eval("sublexic <- import_csv(\"" + _rContextLexicon.getCsvPath() + "\")");
            _rConnection.eval("res <- specificities.lexicon(lexic,sublexic,sumCol,tabCol)");
            _rConnection.eval("res <- res[,1]");
            _rConnection.eval("names(res) <- names(lexic)");
            _rConnection.eval("res <- res[match(names(sublexic),names(res))]");
            _rConnection.eval("names(res) <- NULL");
            _rConnection.eval("export_csv(list(res),\"" + _rResultPath + "\")");
        } catch (RserveException e) {
            _logger.error("cannot execute R command : " + _rConnection.getLastError()
                    ,e);
        }
        return resToFloat();
    }


    private List<Float> resToFloat() {
        List<Float> floatArray = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(_rResultPath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (Objects.equals(sCurrentLine, "Inf")){
                    floatArray.add(Float.POSITIVE_INFINITY);
                    _logger.error("positive infinity was return by R : " + _id);
                }
                else if (Objects.equals(sCurrentLine, "-Inf")){
                    floatArray.add(Float.NEGATIVE_INFINITY);
                    _logger.error("negative infinity was return by R : " + _id);
                }
                else {
                    floatArray.add(Float.parseFloat(sCurrentLine));
                }
            }
            br.close();
        }
        catch (IOException e) {
            _logger.error("cannot read result of R : ",e);
        }
        return floatArray;
    }

    /*
    run execute method
     */
    @Override
    public void run() {
        _logger.info("compute specificities coefficient for : " + _id);
        _rConnection = _rConnectionPool.getRConnection(Thread.currentThread());
        execute();
        _rConnectionPool.releaseThread(Thread.currentThread());
        _logger.info("specificities coefficient is computed for : " + _id);
    }
}
