package module.termsuite.terminology;

import models.MorphoSyntaxOffsetId;
import models.TerminologyOffetId;

import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyStandOff {
    private final String id;
    private final List<MorphoSyntaxOffsetId> morpho;
    private final List<TerminologyOffetId> termino;

    public TerminologyStandOff(String id, List<MorphoSyntaxOffsetId> morpho, List<TerminologyOffetId> termino) {

        this.id = id;
        this.morpho = morpho;
        this.termino = termino;
    }

    public List<TerminologyOffetId> getTermino() {
        return termino;
    }

    public void execute() {
    }
}
