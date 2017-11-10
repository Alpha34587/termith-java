import org.atilf.delegate.disambiguation.txm.TxmContextExtractorDelegate;
import org.atilf.models.disambiguation.TxmContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.*;

public class TxmContextDelegateTest extends  IntegrationTasks {
    private TxmContextExtractorDelegate _t = new TxmContextExtractorDelegate();
    private String _expectedString =
            "[[NOM, pêche, pêche, 26][VER:pres, être, est, 27][DET:ART, un, une, 28][NOM, pêche, pêche, 29]" +
                    "[ADJ, côtier, côtière, 30][PUN, ,, ,, 31][VER:pper, limiter, limite, 32][PRP, à, à, 33]" +
                    "[PRO:IND, quelque, quelques, 34][NOM, espèce, espèces, 35][ADJ, commun, communes, 36]" +
                    "[SENT, ., ., 37][PRO:PER, il, il, 38][NOM, pêche, pêche, 26][VER:pres, être, est, 27]" +
                    "[DET:ART, un, une, 28][NOM, pêche, pêche, 29][ADJ, côtier, côtière, 30][PUN, ,, ,, 31]" +
                    "[VER:pper, limiter, limite, 32][PRP, à, à, 33][PRO:IND, quelque, quelques, 34]" +
                    "[NOM, espèce, espèces, 35][ADJ, commun, communes, 36][SENT, ., ., 37][PRO:PER, il, il, 38]" +
                    "[NOM, pêche, pêche, 26][VER:pres, être, est, 27][DET:ART, un, une, 28][NOM, pêche, pêche, 29]" +
                    "[ADJ, côtier, côtière, 30][PUN, ,, ,, 31][VER:pper, limiter, limite, 32][PRP, à, à, 33]" +
                    "[PRO:IND, quelque, quelques, 34][NOM, espèce, espèces, 35][ADJ, commun, communes, 36]" +
                    "[SENT, ., ., 37][PRO:PER, il, il, 38]]";
    @Before
    public void setUp() throws Exception {
        _t.setOutputPath(Paths.get("src/integrationTest/resources/txm"));
        _t.setAnnotation("#DM4");
    }

    @Ignore
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);

        _termithIndex.getTermsTxmContext().forEach(
                (k,v) -> Assert.assertEquals(
                        "the context must be contains these strings values",
                        _expectedString,
                        toStringContext(v))
        );
    }
    public String toStringContext(List<TxmContext> txmContextList) {
        Collections.sort(txmContextList, Comparator.comparingInt(one -> one.getTxmWord().size()));
        String observedString = "[";
        for (TxmContext t : txmContextList) {
            while (!t.isEmpty()){
                observedString += (t.popTxmWord().values().toString());
            }
        }
        observedString += "]";
        return observedString;
    }
}