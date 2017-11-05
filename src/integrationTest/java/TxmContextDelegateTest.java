import org.atilf.delegate.disambiguation.txm.TxmContextExtractorDelegate;
import org.atilf.models.disambiguation.TxmContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TxmContextDelegateTest extends  IntegrationTasks {
    TxmContextExtractorDelegate _t = new TxmContextExtractorDelegate();
    String expectedString = "[[NOM, pêche, pêche, 26][VER:pres, être, est, 27][DET:ART, un, une, 28]" +
            "[NOM, pêche, pêche, 26][VER:pres, être, est, 27][DET:ART, un, une, 28]" +
            "[NOM, pêche, pêche, 26][VER:pres, être, est, 27][DET:ART, un, une, 28]" +
            "[NOM, pêche, pêche, 29][ADJ, côtier, côtière, 30][PUN, ,, ,, 31]" +
            "[VER:pper, limiter, limite, 32][PRP, à, à, 33][PRO:IND, quelque, quelques, 34]" +
            "[NOM, espèce, espèces, 35][ADJ, commun, communes, 36][SENT, ., ., 37][PRO:PER, il, il, 38]" +
            "[NOM, pêche, pêche, 29][ADJ, côtier, côtière, 30][PUN, ,, ,, 31][VER:pper, limiter, limite, 32]" +
            "[PRP, à, à, 33][PRO:IND, quelque, quelques, 34][NOM, espèce, espèces, 35]" +
            "[ADJ, commun, communes, 36][SENT, ., ., 37][PRO:PER, il, il, 38][NOM, pêche, pêche, 29]" +
            "[ADJ, côtier, côtière, 30][PUN, ,, ,, 31][VER:pper, limiter, limite, 32][PRP, à, à, 33]" +
            "[PRO:IND, quelque, quelques, 34][NOM, espèce, espèces, 35][ADJ, commun, communes, 36]" +
            "[SENT, ., ., 37][PRO:PER, il, il, 38]]";
    @Before
    public void setUp() throws Exception {
        _t.setOutputPath(Paths.get("src/integrationTest/resources/txm"));
        _t.setAnnotation("#DM4");
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
        _termithIndex.getTermsTxmContext().forEach(
                (k,v) -> Assert.assertEquals(
                        "the context must be contains these strings value",
                        expectedString
                        ,toStringContext(v))
        );
    }
    public String toStringContext(List<TxmContext> txmContextList) {
        String observedString = "[";
        for (TxmContext t : txmContextList) {
            while (!t.isEmpty()){
                observedString += (t.getTxmWord().values().toString());
            }
        }
        observedString += "]";
        return observedString;
    }
}