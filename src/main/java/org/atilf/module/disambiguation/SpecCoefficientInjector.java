package org.atilf.module.disambiguation;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.models.termith.TermithIndex;

/**
 * compute specificity coefficient for each word of a term candidate entry. the result is retained on the
 * _lexicalProfile field. This class call a R script with the library Rcaller.
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoefficientInjector implements Runnable{
    private final LexiconProfile _lexiconProfile;
    private final RLexicon _rLexicon;
    private final RLexicon _rContextLexicon;
    private final CorpusLexicon _corpusLexicon;

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
    public SpecCoefficientInjector(LexiconProfile lexiconProfile, RLexicon rLexicon, CorpusLexicon corpusLexicon) {

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
        _corpusLexicon = termithIndex.getCorpusLexicon();
        _lexiconProfile = termithIndex.getContextLexicon().get(id);
        _rLexicon = rLexicon;
        _rContextLexicon = new RLexicon(_lexiconProfile, _corpusLexicon);
    }

    /**
     * call reduceToLexicalProfile method
     */
    public void execute() {
        reduceToLexicalProfile(computeSpecCoefficient());
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
        code.addRCode("source(" + "\"src/main/resources/disambiguation/R/specificities.R\"" + ")");
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
        code.addRCode("names(sublexic) <-" + _rContextLexicon.getROcc());
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
        return rcaller.getParser().getAsFloatArray("res");
    }

    /*
    run execute method
     */
    @Override
    public void run() {
        execute();
    }
}
