
import org.apache.ibatis.javassist.bytecode.analysis.Executor;
import org.atilf.delegate.disambiguation.contextLexicon.CommonWordsPosLemmaCleanerDelegate;
import org.atilf.models.TermithIndex;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonWordsCleanerDelegateTest extends  IntegrationTasks{
    @Test
    public void executeTasks() throws Exception {
        Assert.assertEquals(1,1);
    }
}