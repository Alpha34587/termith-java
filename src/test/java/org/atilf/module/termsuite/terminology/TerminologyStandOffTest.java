package org.atilf.module.termsuite.terminology;

import org.atilf.models.termsuite.MorphologyOffsetId;
import org.atilf.models.termsuite.TermOffsetId;
import org.atilf.module.enrichment.analyze.TerminologyStandOff;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 15/09/16.
 */
public class TerminologyStandOffTest {


    private static TerminologyStandOff _observedSimpleStandOff;
    private static  TerminologyStandOff _observedMultiStandOff;
    private static  TerminologyStandOff _observedSymbolStandOff;

    @BeforeClass
    public static void setUp(){
        List<MorphologyOffsetId> morphology = new ArrayList<>();
        List<TermOffsetId> _termino = new ArrayList<>();
        List<MorphologyOffsetId> _multimorpho = new ArrayList<>();
        List<TermOffsetId> _multitermino = new ArrayList<>();
        List<MorphologyOffsetId> _symbolmorpho = new ArrayList<>();
        List<TermOffsetId> _symboltermino = new ArrayList<>();

        /*
        simple test
         */
        morphology.add(new MorphologyOffsetId(0,10,"cuillière","N",0));
        morphology.add(new MorphologyOffsetId(11,13,"en","DET",1));
        morphology.add(new MorphologyOffsetId(14,18,"bois","N",2));
        morphology.add(new MorphologyOffsetId(15,19,"rien","N",3));
        morphology.add(new MorphologyOffsetId(20,26,"pierre","N",4));

        _termino.add(new TermOffsetId(0,10,0,"cuillière"));
        _termino.add(new TermOffsetId(0,18,1,"cuillière en bois"));
        _termino.add(new TermOffsetId(20,26,2,"pierre"));
        _observedSimpleStandOff = new TerminologyStandOff(morphology, _termino);

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

        _multitermino.add(new TermOffsetId(0,10,0,"cuillière"));
        _multitermino.add(new TermOffsetId(0,18,1,"cuillière en bois"));
        _multitermino.add(new TermOffsetId(20,26,2,"pierre"));
        _observedMultiStandOff = new TerminologyStandOff(_multimorpho, _multitermino);

        /*
        symbol test
         */
        _symbolmorpho.add(new MorphologyOffsetId(0,10,"cuillière","N",0));
        _symbolmorpho.add(new MorphologyOffsetId(11,13,"en","DET",1));
        _symbolmorpho.add(new MorphologyOffsetId(14,18,"bois","N",woodIds));
        _symbolmorpho.add(new MorphologyOffsetId(18,19,",","N",woodIds));
        _symbolmorpho.add(new MorphologyOffsetId(15,19,"rien","N",4));
        _symbolmorpho.add(new MorphologyOffsetId(20,26,"pierre","N",stoneIds));

        _symboltermino.add(new TermOffsetId(0,10,0,"cuillière"));
        _symboltermino.add(new TermOffsetId(0,18,1,"cuillière en bois"));
        _symboltermino.add(new TermOffsetId(20,26,2,"pierre"));
        _observedSymbolStandOff = new TerminologyStandOff(_symbolmorpho, _symboltermino);
    }
    @Test
    public void simpleExecute() throws Exception {
        _observedSimpleStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2]");
        expected.add("[4]");

        Assert.assertFalse("the list of index must be not empty",
                _observedSimpleStandOff.getTerminology().isEmpty());
        
        _observedSimpleStandOff.getTerminology().forEach(
                ids -> Assert.assertEquals("_morphology ids must be equals",
                        expected.get(_observedSimpleStandOff.getTerminology().indexOf(ids)),
                        ids.getIds().toString()
                        )
        );

    }

    @Test
    public void multiExecute() throws Exception {
        _observedMultiStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");

        Assert.assertFalse("the list of index must be not empty",
                _observedMultiStandOff.getTerminology().isEmpty());
        
        _observedMultiStandOff.getTerminology().forEach(
                ids -> Assert.assertEquals("_morphology ids must be equals",
                        expected.get(_observedMultiStandOff.getTerminology().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

    @Test
    public void symbolExecute() throws Exception {
        _observedSymbolStandOff.execute();
        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2, 3]");
        expected.add("[5, 6, 7, 8]");
        
        Assert.assertFalse("the list of index must be not empty",
                _observedSymbolStandOff.getTerminology().isEmpty());
        
        _observedSymbolStandOff.getTerminology().forEach(
                ids -> Assert.assertEquals("_morphology ids must be equals",
                        expected.get(_observedSymbolStandOff.getTerminology().indexOf(ids)),
                        ids.getIds().toString()
                )
        );

    }

}