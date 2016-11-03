package org.atilf.module.termsuite.terminology;

import org.atilf.models.termsuite.MorphoSyntaxOffsetId;
import org.atilf.models.termsuite.TermsOffsetId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 15/09/16.
 */
public class TerminologyStandOffTest {

    private List<MorphoSyntaxOffsetId> morpho;
    private List<TermsOffsetId> termino;
    private TerminologyStandOff simpleTerminologyStandOff;
    private TerminologyStandOff multiTerminologyStandOff;
    private List<MorphoSyntaxOffsetId> multimorpho;
    private List<TermsOffsetId> multitermino;
    private List<MorphoSyntaxOffsetId> symbolmorpho;
    private List<TermsOffsetId> symboltermino;
    private TerminologyStandOff symbolTerminologyStandOff;

    @Before
    public void setUp(){
        morpho = new ArrayList<>();
        morpho.add(new MorphoSyntaxOffsetId(0,10,"cuillière","N",0));
        morpho.add(new MorphoSyntaxOffsetId(11,13,"en","DET",1));
        morpho.add(new MorphoSyntaxOffsetId(14,18,"bois","N",2));
        morpho.add(new MorphoSyntaxOffsetId(15,19,"rien","N",3));
        morpho.add(new MorphoSyntaxOffsetId(20,26,"pierre","N",4));

        termino = new ArrayList<>();
        termino.add(new TermsOffsetId(0,10,0,"cuillière"));
        termino.add(new TermsOffsetId(0,18,1,"cuillière en bois"));
        termino.add(new TermsOffsetId(20,26,2,"pierre"));
        simpleTerminologyStandOff = new TerminologyStandOff(morpho,termino);

        multimorpho = new ArrayList<>();
        multimorpho.add(new MorphoSyntaxOffsetId(0,10,"cuillière","N",0));
        List<Integer> boisIds = new ArrayList<>();
        boisIds.add(2);
        boisIds.add(3);
        List<Integer> pierreIds = new ArrayList<>();
        pierreIds.add(5);
        pierreIds.add(6);
        pierreIds.add(7);
        pierreIds.add(8);
        multimorpho.add(new MorphoSyntaxOffsetId(11,13,"en","DET",1));
        multimorpho.add(new MorphoSyntaxOffsetId(14,18,"bois","N",boisIds));
        multimorpho.add(new MorphoSyntaxOffsetId(15,19,"rien","N",4));
        multimorpho.add(new MorphoSyntaxOffsetId(20,26,"pierre","N",pierreIds));

        multitermino = new ArrayList<>();
        multitermino.add(new TermsOffsetId(0,10,0,"cuillière"));
        multitermino.add(new TermsOffsetId(0,18,1,"cuillière en bois"));
        multitermino.add(new TermsOffsetId(20,26,2,"pierre"));
        multiTerminologyStandOff = new TerminologyStandOff(multimorpho,multitermino);

        symbolmorpho = new ArrayList<>();
        symbolmorpho.add(new MorphoSyntaxOffsetId(0,10,"cuillière","N",0));
        symbolmorpho.add(new MorphoSyntaxOffsetId(11,13,"en","DET",1));
        symbolmorpho.add(new MorphoSyntaxOffsetId(14,18,"bois","N",boisIds));
        symbolmorpho.add(new MorphoSyntaxOffsetId(18,19,",","N",boisIds));
        symbolmorpho.add(new MorphoSyntaxOffsetId(15,19,"rien","N",4));
        symbolmorpho.add(new MorphoSyntaxOffsetId(20,26,"pierre","N",pierreIds));

        symboltermino = new ArrayList<>();
        symboltermino.add(new TermsOffsetId(0,10,0,"cuillière"));
        symboltermino.add(new TermsOffsetId(0,18,1,"cuillière en bois"));
        symboltermino.add(new TermsOffsetId(20,26,2,"pierre"));
        symbolTerminologyStandOff = new TerminologyStandOff(symbolmorpho,symboltermino);
    }
    @Test
    public void simpleExecute() throws Exception {
        simpleTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2]");
        expected.add("[4]");

        simpleTerminologyStandOff.get_termino().forEach(
                ids -> Assert.assertEquals("morpho ids must be equals",
                        expected.get(simpleTerminologyStandOff.get_termino().indexOf(ids)),
                        ids.getIds().toString()
                        )
        );

    }

    @Test
    public void multiExecute() throws Exception {
        multiTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");

        multiTerminologyStandOff.get_termino().forEach(
                ids -> Assert.assertEquals("morpho ids must be equals",
                        expected.get(multiTerminologyStandOff.get_termino().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

    @Test
    public void symbolExecute() throws Exception {
        multiTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");

        multiTerminologyStandOff.get_termino().forEach(
                ids -> Assert.assertEquals("morpho ids must be equals",
                        expected.get(multiTerminologyStandOff.get_termino().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

}