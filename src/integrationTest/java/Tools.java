import org.atilf.models.TermithIndex;
import org.atilf.runner.RunnerBuilder;

public class Tools {

    static void createAndExecuteRunner(TermithIndex termithIndex, String bpmnPath) throws Exception {
        new RunnerBuilder()
                .setTermithIndex(termithIndex)
                .setBpmnDiagram(bpmnPath)
                .createRunner().execute();
    }
}
