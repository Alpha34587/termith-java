package org.atilf.module.disambiguation;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.atilf.models.disambiguation.GlobalLexicon;
import org.atilf.models.disambiguation.LexicalProfile;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.models.termith.TermithIndex;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoefficientInjector implements Runnable{
    private final LexicalProfile _lexicalProfile;
    private final RLexicon _rLexicon;
    private final RLexicon _rSubLexicon;
    private final GlobalLexicon _globalLexicon;

    public SpecCoefficientInjector(LexicalProfile lexicalProfile, RLexicon rLexicon, GlobalLexicon globalLexicon) {

        _globalLexicon = globalLexicon;
        _lexicalProfile = lexicalProfile;
        _rLexicon = rLexicon;
        _rSubLexicon = new RLexicon(lexicalProfile, globalLexicon);
    }

    public SpecCoefficientInjector(String p,TermithIndex termithIndex, RLexicon rLexicon){
        _globalLexicon = termithIndex.getGlobalLexicon();
        _lexicalProfile = termithIndex.getContextLexicon().get(p);
        _rLexicon = rLexicon;
        _rSubLexicon = new RLexicon(_lexicalProfile, _globalLexicon);
    }

    public void execute() {
        reduceToLexicalProfile(computeSpecCoefficient());
    }

    private void reduceToLexicalProfile(float[] specCoefficient) {
        int cnt = 0;
        for (String id : _rSubLexicon.getIdSubCorpus()) {
            _lexicalProfile.addCoefficientSpec(
                    _globalLexicon.getLexicalEntry(Integer.parseInt(id)),
                    specCoefficient[cnt]);
            cnt++;
        }
    }

    float[] computeSpecCoefficient() {
        RCaller rcaller = RCaller.create();
        RCode code = RCode.create();
        code.addRCode("source(" + "\"src/main/resources/disambiguation/R/specificities.R\"" + ")");
        code.addRCode("sumCol <-" + _rLexicon.getCorpusSizeOcc());
        code.addRCode("tabCol <-" + "c(" + _rSubLexicon.getCorpusSizeOcc() + "," + _rLexicon.getCorpusSizeOcc() + ")");
        code.addRCode("names(tabCol) <- c(\"sublexicon\",\"complementary\")");
        code.addRCode("lexic <-" + _rLexicon.getROcc());
        code.addRCode("names(lexic) <-" + _rLexicon.getRName());
        code.addRCode("sublexic <-" + _rSubLexicon.getROcc());
        code.addRCode("names(sublexic) <-" + _rSubLexicon.getROcc());
        code.addRCode("res <- specificities.lexicon(lexic,sublexic,sumCol,tabCol)");
        code.addRCode("res <- res[,1]");
        code.addRCode("names(res) <- names(lexic)");
        code.addRCode("res <- res[match(names(sublexic),names(res))]");
        code.addRCode("names(res) <- NULL");
        rcaller.setRCode(code);
        rcaller.runAndReturnResult("res");
        return rcaller.getParser().getAsFloatArray("res");
    }

    @Override
    public void run() {
        this.execute();
    }
}
