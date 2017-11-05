import org.atilf.delegate.disambiguation.txm.TxmContextExtractorDelegate;
import org.atilf.models.disambiguation.TxmContext;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class TxmContextDelegateTest extends  IntegrationTasks {
    TxmContextExtractorDelegate _t = new TxmContextExtractorDelegate();
    @Before
    public void setUp() throws Exception {
        _t.setOutputPath(Paths.get("src/integrationTest/resources/txm"));
        _t.setAnnotation("#DM4");
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
        _termithIndex.getTermsTxmContext();
    }
    public String toStringContext(TxmContext txmContext) {
    }
}