package org.atilf.module.disambiguisation;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.atilf.models.disambiguisation.GlobalLexic;
import org.atilf.models.disambiguisation.RLexic;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoefficientInjector {
    private final LexicalProfile _lexicalProfile;
    private final RLexic _rLexic;
    private final RLexic _rSubLexic;
    private final GlobalLexic _globalLexic;

    public SpecCoefficientInjector(LexicalProfile lexicalProfile, RLexic rLexic, GlobalLexic globalLexic) {

        _globalLexic = globalLexic;
        _lexicalProfile = lexicalProfile;
        _rLexic = rLexic;
        _rSubLexic = new RLexic(lexicalProfile,globalLexic);
    }

    public void execute() {
        reduceToLexicalProfile(computeSpecCoefficient());
    }

    private void reduceToLexicalProfile(float[] specCoef) {
        int cnt = 0;
        for (String id : _rSubLexic.get_idSubCorpus()) {
            _lexicalProfile.addCoefficientSpec(
                    _globalLexic.getLexicalEntry(Integer.parseInt(id)),
                    specCoef[cnt]);
            cnt++;
        }
    }

    public float[] computeSpecCoefficient() {
        RCaller rcaller = RCaller.create();
        RCode code = RCode.create();
        code.addRCode("source(" + "\"src/main/resources/disambiguation/R/specificities.R\"" + ")");
        code.addRCode("sumCol <-" + _rLexic.getCorpusSizeOcc());
        code.addRCode("tabCol <-" + "c(" + _rSubLexic.getCorpusSizeOcc() + "," + _rLexic.getCorpusSizeOcc() + ")");
        code.addRCode("names(tabCol) <- c(\"sublexicon\",\"complementary\")");
        code.addRCode("lexic <-" + _rLexic.get_rOcc());
        code.addRCode("names(lexic) <-" + _rLexic.get_rName());
        code.addRCode("sublexic <-" + _rSubLexic.get_rOcc());
        code.addRCode("names(sublexic) <-" + _rSubLexic.get_rOcc());
        code.addRCode("res <- specificities.lexicon(lexic,sublexic,sumCol,tabCol)");
        code.addRCode("res <- res[,1]");
        code.addRCode("names(res) <- names(lexic)");
        code.addRCode("res <- res[match(names(sublexic),names(res))]");
        code.addRCode("names(res) <- NULL");
        rcaller.setRCode(code);
        rcaller.runAndReturnResult("res");
        return rcaller.getParser().getAsFloatArray("res");
    }
}
