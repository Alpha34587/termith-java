package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.TxmContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        _txmContext1.addElements("26","pêche","NOM","pêche");
        _txmContext1.addElements("27","être","VER:pres","est");
        _txmContext1.addElements("28","un","DET:ART","une");

        _txmContext2.addElements("29","pêche","NOM","pêche");
        _txmContext2.addElements("30","côtier","ADJ","côtière");
        _txmContext2.addElements("31",",","PUN",",");
        _txmContext2.addElements("32","limiter","VER:pper","limite");
        _txmContext2.addElements("33","à","PRP","à" );
        _txmContext2.addElements("34","quelque","PRO:IND","quelques");
        _txmContext2.addElements("35","espèce","NOM","espèces");
        _txmContext2.addElements("36","commun","ADJ","communes");
        _txmContext2.addElements("37",".","SENT",".");
        _txmContext2.addElements("38","il","PRO:PER","il");

        _txmContext1.setFilename("src/test/resources/module/disambiguation/txm/txmContextExtractor/test1.xml");
        _txmContext2.setFilename("src/test/resources/module/disambiguation/txm/txmContextExtractor/test1.xml");

        _expectedTxmContexts.add(_txmContext1);
        _expectedTxmContexts.add(_txmContext2);

    }

    @Test
    public void extractTxmContext() throws Exception {
        new TxmContextExtractor("src/test/resources/module/disambiguation/txm/txmContextExtractor/test1.xml",
                _observedTxmContexts,"#DM4")
                .execute();
        int i = 0;
        for (TxmContext expectedTxmContext : _expectedTxmContexts) {
            while(!expectedTxmContext.isEmpty()){
                Assert.assertEquals("this must two elements must be equals", ToString(expectedTxmContext),
                        ToString(_observedTxmContexts.get("entry-7263_DM4").get(i)));
            }
            i++;
        }
    }

    private String ToString(TxmContext txmContext){
        Map<String,String> map = txmContext.getTxmWord();
        return txmContext.getFilename() +
                " " + map.get("target") +
                " " + map.get("lemma") +
                " " + map.get("pos") +
                " " + map.get("word");
    }
}
