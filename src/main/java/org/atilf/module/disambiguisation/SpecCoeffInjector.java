package org.atilf.module.disambiguisation;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.atilf.models.GlobalLexic;
import org.atilf.models.RLexic;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffInjector {
    private final LexicalProfile lexicalProfile;
    private final RLexic rLexic;
    private final RLexic rSubLexic;
    private final GlobalLexic globalLexic;

    public SpecCoeffInjector(LexicalProfile lexicalProfile, RLexic rLexic, GlobalLexic globalLexic) {

        this.globalLexic = globalLexic;
        this.lexicalProfile = lexicalProfile;
        this.rLexic = rLexic;
        this.rSubLexic = new RLexic(lexicalProfile.getLexicalTable(),globalLexic);
    }

    public void execute() {
        ReduceToLexicalProfile(computeSpecCoeff());
    }

    public void ReduceToLexicalProfile(float[] specCoef) {
        int cnt = 0;
        for (String id : rSubLexic.getIdSubCorpus()) {
            lexicalProfile.addCoefficientSpec(
                    globalLexic.getLexicalEntry(Integer.parseInt(id)),
                    specCoef[cnt]);
            cnt++;
        }
    }

    public float[] computeSpecCoeff() {
        RCaller rcaller = RCaller.create();
        RCode code = RCode.create();
        code.addRCode("source(" + "\"src/main/resources/disambiguisation/R/specificities.R\"" + ")");
        code.addRCode("sumCol <-" + rLexic.getCorpusSizeOcc());
        code.addRCode("tabCol <-" + "c(" + rSubLexic.getCorpusSizeOcc() + "," + rLexic.getCorpusSizeOcc() + ")");
        code.addRCode("names(tabCol) <- c(\"sublexicon\",\"complementary\")");
        code.addRCode("lexic <-" + rLexic.getrOcc());
        code.addRCode("names(lexic) <-" + rLexic.getrName());
        code.addRCode("sublexic <-" + rSubLexic.getrOcc());
        code.addRCode("names(sublexic) <-" + rSubLexic.getrOcc());
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
