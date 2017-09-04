
import org.atilf.models.TermithIndex;
import org.junit.Test;
public class CommonWordsPosLemmaCleanerDelegateIntegrationTest {
    @Test
    public void executeTasks() throws Exception {
                TermithIndex termithIndex= new TermithIndex();
                String bpmnPath = "";
                Tools.createAndExecuteRunner(termithIndex, bpmnPath);

    }
}