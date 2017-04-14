package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.TxmContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmContextExtractorTest {

    private static HashMap<String,List<TxmContext>> _observedTxmContexts = new HashMap<>();
    private static List<TxmContext> _expectedTxmContexts = new ArrayList<>();
    private static List<String> includeElements = new ArrayList<>();


    @BeforeClass
    public static void setUp(){

        TxmContext _txmContext1 = new TxmContext();
        TxmContext _txmContext2 = new TxmContext();

        _txmContext1.addElements("t26","pêche","NOM","pêche");
        _txmContext1.addElements("t27","être","VER:pres","est");
        _txmContext1.addElements("t28","un","DET:ART","une");

        _txmContext2.addElements("t29","pêche","NOM","pêche");
        _txmContext2.addElements("t30","côtier","ADJ","côtière");
        _txmContext2.addElements("t32",",","PUN",".");
        _txmContext2.addElements("t32","limiter","VER:pper","limite");
        _txmContext2.addElements("t33","à","PRP","à" );
        _txmContext2.addElements("t34","quelque","PRO:IND","quelques");
        _txmContext2.addElements("t35","espèce","NOM","espèces");
        _txmContext2.addElements("t36","commun","ADJ","communes");
        _txmContext2.addElements("t37",".","SENT",".");
        _txmContext2.addElements("t38","il","PRO:PER","il");

        _expectedTxmContexts.add(_txmContext1);
        _expectedTxmContexts.add(_txmContext2);

    }

    @Test
    public void extractLexiconProfile() throws Exception {
        new TxmContextExtractor("src/test/resources/module/disambiguation/txm/test1.xml",_observedTxmContexts).execute();
        int i = 0;
        for (TxmContext expectedTxmContext : _expectedTxmContexts) {
            while(!expectedTxmContext.isEmpty()){
                Assert.assertEquals("this must two elements must be equals", ToString(expectedTxmContext),
                        ToString(_observedTxmContexts.get("entry-7263_DM4").get(i)));
            }
            i++;
        }
    }

    public String ToString(TxmContext txmContext){
        return txmContext.getFilename() +
                " " + txmContext.getTarget() +
                " " + txmContext.getLemma() +
                " " + txmContext.getPos() +
                " " + txmContext.getWord();
    }
}
