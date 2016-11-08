package org.atilf.module.termsuite.terminology;

import org.atilf.models.termsuite.MorphologyOffsetId;
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

    private List<MorphologyOffsetId> _morpho = new ArrayList<>();
    private List<TermsOffsetId> _termino = new ArrayList<>();
    private List<MorphologyOffsetId> _multimorpho = new ArrayList<>();
    private List<TermsOffsetId> _multitermino = new ArrayList<>();
    private List<MorphologyOffsetId> _symbolmorpho = new ArrayList<>();
    private List<TermsOffsetId> _symboltermino = new ArrayList<>();
    private TerminologyStandOff _simpleTerminologyStandOff;
    private TerminologyStandOff _multiTerminologyStandOff;
    private TerminologyStandOff _symbolTerminologyStandOff;

    @Before
    public void setUp(){

        /*
        simple test
         */
        _morpho.add(new MorphologyOffsetId(0,10,"cuillière","N",0));
        _morpho.add(new MorphologyOffsetId(11,13,"en","DET",1));
        _morpho.add(new MorphologyOffsetId(14,18,"bois","N",2));
        _morpho.add(new MorphologyOffsetId(15,19,"rien","N",3));
        _morpho.add(new MorphologyOffsetId(20,26,"pierre","N",4));

        _termino.add(new TermsOffsetId(0,10,0,"cuillière"));
        _termino.add(new TermsOffsetId(0,18,1,"cuillière en bois"));
        _termino.add(new TermsOffsetId(20,26,2,"pierre"));
        _simpleTerminologyStandOff = new TerminologyStandOff(_morpho, _termino);

        /*
        multi test
         */
        List<Integer> woodIds = new ArrayList<>();
        woodIds.add(2);
        woodIds.add(3);
        List<Integer> stoneIds = new ArrayList<>();
        stoneIds.add(5);
        stoneIds.add(6);
        stoneIds.add(7);
        stoneIds.add(8);
        _multimorpho.add(new MorphologyOffsetId(0,10,"cuillière","N",0));
        _multimorpho.add(new MorphologyOffsetId(11,13,"en","DET",1));
        _multimorpho.add(new MorphologyOffsetId(14,18,"bois","N",woodIds));
        _multimorpho.add(new MorphologyOffsetId(15,19,"rien","N",4));
        _multimorpho.add(new MorphologyOffsetId(20,26,"pierre","N",stoneIds));

        _multitermino.add(new TermsOffsetId(0,10,0,"cuillière"));
        _multitermino.add(new TermsOffsetId(0,18,1,"cuillière en bois"));
        _multitermino.add(new TermsOffsetId(20,26,2,"pierre"));
        _multiTerminologyStandOff = new TerminologyStandOff(_multimorpho, _multitermino);

        /*
        symbol test
         */
        _symbolmorpho.add(new MorphologyOffsetId(0,10,"cuillière","N",0));
        _symbolmorpho.add(new MorphologyOffsetId(11,13,"en","DET",1));
        _symbolmorpho.add(new MorphologyOffsetId(14,18,"bois","N",woodIds));
        _symbolmorpho.add(new MorphologyOffsetId(18,19,",","N",woodIds));
        _symbolmorpho.add(new MorphologyOffsetId(15,19,"rien","N",4));
        _symbolmorpho.add(new MorphologyOffsetId(20,26,"pierre","N",stoneIds));

        _symboltermino.add(new TermsOffsetId(0,10,0,"cuillière"));
        _symboltermino.add(new TermsOffsetId(0,18,1,"cuillière en bois"));
        _symboltermino.add(new TermsOffsetId(20,26,2,"pierre"));
        _symbolTerminologyStandOff = new TerminologyStandOff(_symbolmorpho, _symboltermino);
    }
    @Test
    public void simpleExecute() throws Exception {
        _simpleTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2]");
        expected.add("[4]");

        _simpleTerminologyStandOff.getTerminology().forEach(
                ids -> Assert.assertEquals("_morpho ids must be equals",
                        expected.get(_simpleTerminologyStandOff.getTerminology().indexOf(ids)),
                        ids.getIds().toString()
                        )
        );

    }

    @Test
    public void multiExecute() throws Exception {
        _multiTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");

        _multiTerminologyStandOff.getTerminology().forEach(
                ids -> Assert.assertEquals("_morpho ids must be equals",
                        expected.get(_multiTerminologyStandOff.getTerminology().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

    @Test
    public void symbolExecute() throws Exception {
        _symbolTerminologyStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");

        _symbolTerminologyStandOff.getTerminology().forEach(
                ids -> Assert.assertEquals("_morpho ids must be equals",
                        expected.get(_symbolTerminologyStandOff.getTerminology().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

}