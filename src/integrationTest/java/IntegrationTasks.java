import org.atilf.delegate.Delegate;
import org.atilf.models.TermithIndex;
import org.atilf.runner.RunnerBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class IntegrationTasks {
    TermithIndex _termithIndex = new TermithIndex();

    public <T extends Delegate> void executeTasksTest(T termithModule)
            throws InterruptedException, ExecutionException, IOException {
        termithModule.setTermithIndex(_termithIndex);
        termithModule.setExecutorService(Executors.newFixedThreadPool(4));
        termithModule.executeTasks();
    }
}
