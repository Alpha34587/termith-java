package module.termsuite.terminology;

import models.MorphoSyntaxOffsetId;
import models.TerminologyOffetId;
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
    private List<TerminologyOffetId> termino;
    private TerminologyStandOff simpleTerminologyStandOff;
    private TerminologyStandOff multiTerminologyStandOff;
    private List<MorphoSyntaxOffsetId> multimorpho;
    private List<TerminologyOffetId> multitermino;

    @Before
    public void setUp(){
        morpho.add(new MorphoSyntaxOffsetId(0,10,"cuillière","N",0));
        morpho.add(new MorphoSyntaxOffsetId(11,13,"en","DET",1));
        morpho.add(new MorphoSyntaxOffsetId(14,18,"bois","N",2));
        morpho.add(new MorphoSyntaxOffsetId(15,19,"rien","N",3));
        morpho.add(new MorphoSyntaxOffsetId(20,26,"pierre","N",4));

        termino.add(new TerminologyOffetId(0,10,0,"cuillière"));
        termino.add(new TerminologyOffetId(0,18,1,"cuillière en bois"));
        termino.add(new TerminologyOffetId(20,26,2,"pierre"));
        simpleTerminologyStandOff = new TerminologyStandOff("1",morpho,termino);


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

        multitermino.add(new TerminologyOffetId(0,10,0,"cuillière"));
        multitermino.add(new TerminologyOffetId(0,18,1,"cuillière en bois"));
        multitermino.add(new TerminologyOffetId(20,26,2,"pierre"));
        multiTerminologyStandOff = new TerminologyStandOff("1",multimorpho,multitermino);
    }
    @Test
    public void simpleExecute() throws Exception {
        simpleTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2]");
        expected.add("[4]");

        simpleTerminologyStandOff.getTermino().forEach(
                ids -> Assert.assertEquals("morpho ids must be equals",
                        expected.get(simpleTerminologyStandOff.getTermino().indexOf(ids)),
                        ids.getIds().toString()
                        )
        );

    }

    @Test
    public void multiExecute() throws Exception {
        simpleTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");

        simpleTerminologyStandOff.getTermino().forEach(
                ids -> Assert.assertEquals("morpho ids must be equals",
                        expected.get(simpleTerminologyStandOff.getTermino().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

}