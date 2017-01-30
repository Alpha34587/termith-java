package org.atilf.module.disambiguation.lexiconProfile;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.models.disambiguation.RResources;
import org.atilf.module.Module;

import java.util.Objects;

import static org.atilf.models.disambiguation.AnnotationResources.LEX_OFF;
import static org.atilf.models.disambiguation.AnnotationResources.LEX_ON;

/**
 * compute specificity coefficient for each word of a term candidate entry. the result is retained on the
 * _lexicalProfile field. This class call a R script with the library Rcaller.
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoefficientInjector extends Module {
    private LexiconProfile _lexiconProfile;
    private RLexicon _rLexicon;
    private CorpusLexicon _corpusLexicon;
    private boolean _computeSpecificities = true;
    private RLexicon _rContextLexicon;
    private String _id;

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
    SpecCoefficientInjector(LexiconProfile lexiconProfile, RLexicon rLexicon, CorpusLexicon corpusLexicon) {

        _corpusLexicon = corpusLexicon;
        _lexiconProfile = lexiconProfile;
        _rLexicon = rLexicon;
        /*
        instantiate _rContextLexicon
         */
        _rContextLexicon = new RLexicon(_lexiconProfile, _corpusLexicon);
    }

    /**
     * constructor for SpecCoefficientInjector
     * @param id term entry id of key in lexicalProfile
     * @param termithIndex the termithIndex of a process
     * @param rLexicon the corpusLexicon converted into a RLexicon object that contains the R variable
     *                 of the corpusLexicon and his size
     */
    public SpecCoefficientInjector(String id,TermithIndex termithIndex, RLexicon rLexicon){
        super(termithIndex);
        if (isLexiconPresent(termithIndex,id)) {
            _id = id;
            _corpusLexicon = termithIndex.getCorpusLexicon();
            _lexiconProfile = termithIndex.getContextLexicon().get(id);
            _rLexicon = rLexicon;
            _rContextLexicon = new RLexicon(_lexiconProfile, _corpusLexicon);
            _computeSpecificities = true;
        }
        else {
            _computeSpecificities = false;
        }
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
        _logger.info("compute specificities coefficient for : " + _id);
        try {
            if (_computeSpecificities) {
                reduceToLexicalProfile(computeSpecCoefficient());
            }
            else {
                _logger.info("only terminology or non-terminology lexicon profile is present, " +
                        "no need to compute coefficients for terminology entry : ", _id);
            }
        }
        catch (Exception e){
            _logger.error("problem during the execution of SpecCoefficientInjector :", e);
        }
        _logger.info("specificities coefficient is computed for : " + _id);
    }


    /**
     * add specificityCoefficient calculated at each word in the _lexicalProfile
     * @param specificityCoefficient this float array contains specificities coefficients for all words
     *                               of a lexical profile
     */
    private void reduceToLexicalProfile(float[] specificityCoefficient) {
        int cnt = 0;
        for (String id : _rContextLexicon.getIdContextLexicon()) {
            _lexiconProfile.addCoefficientSpec(
                    _corpusLexicon.getLexicalEntry(Integer.parseInt(id)),
                    specificityCoefficient[cnt]);
            cnt++;
        }
    }

    /**
     * this method calls the R script and compute specificity coefficient for each word of a context
     * @return coefficients specificities
     */
    float[] computeSpecCoefficient() {
        _logger.debug("compute specificity coefficient");
        /*
        instantiate rcaller
         */
        RCaller rcaller = RCaller.create();
        /*
        instantiate rcode and add the script, add some variable and execute specificities.lexicon function
         */
        RCode code = RCode.create();
        /*
        import R script
         */
        code.addRCode(RResources.SCRIPT.toString());
        /*
        add variable
         */
        /*
        add size of the corpus
         */
        code.addRCode("sumCol <-" + _rLexicon.getSize());
        /*
        add size of the corpus and the size of the context
         */
        code.addRCode("tabCol <-" + "c(" + _rContextLexicon.getSize() + "," + _rLexicon.getSize() + ")");
        code.addRCode("names(tabCol) <- c(\"sublexicon\",\"complementary\")");
        /*
        add occurrences numbers for all words of the corpus
         */
        code.addRCode("lexic <-" + _rLexicon.getROcc());
        /*
        add all words of the corpus
         */
        code.addRCode("names(lexic) <-" + _rLexicon.getRName());
        /*
        add occurrences numbers for all words of the context
         */
        code.addRCode("sublexic <-" + _rContextLexicon.getROcc());
        /*
        add all words of the corpus
         */
        code.addRCode("names(sublexic) <-" + _rContextLexicon.getRName());
        /*
        compute specificities coefficient for all words of the corpus
         */
        code.addRCode("res <- specificities.lexicon(lexic,sublexic,sumCol,tabCol)");
        /*
        retained only specificities for words in the context
         */
        code.addRCode("res <- res[,1]");
        code.addRCode("names(res) <- names(lexic)");
        code.addRCode("res <- res[match(names(sublexic),names(res))]");
        code.addRCode("names(res) <- NULL");
        /*
        execute script
         */
        rcaller.setRCode(code);
        rcaller.runAndReturnResult("res");
        rcaller.deleteTempFiles();
        _logger.debug("specificity coefficient has been computed");
        return resToFloat(rcaller.getParser().getAsStringArray("res"));
    }

    private float[] resToFloat(String[] res) {
        float[] floatArray = new float[res.length];
        for (int i = 0; i < res.length; i++){
            if (Objects.equals(res[i], "Inf")){
                floatArray[i] = Float.POSITIVE_INFINITY;
                _logger.error("positive infinity was return by R");
            }
            else if (Objects.equals(res[i], "-Inf")){
                floatArray[i] = Float.NEGATIVE_INFINITY;
                _logger.error("negative infinity was return by R");
            }
            else {
                floatArray[i] = Float.parseFloat(res[i]);
            }
        }
        return floatArray;
    }
}
