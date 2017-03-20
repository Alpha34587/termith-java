package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.atilf.monitor.observer.MemoryPerformanceEvent;
import org.atilf.monitor.observer.TimePerformanceEvent;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create some Delegate inherited classes and executeTasks each of them linearly.
 * @author Simon Meoni Created on 10/11/16.
 */
public class Runner {

    public static TermithIndex _termithIndex;
    public static int _poolSize;
    private static String _bpmnDiagram;

    /*
    common parameter
     */

    private static Path _base;
    private static Path _out;

    /*
    terminology extraction parameter
     */
    private static String _lang;
    private static String _treeTaggerHome;

    /*
    Disambiguation parameter
     */
    private static Path _learningPath;
    private static Path _evaluationPath;
    private static Path _scorePath;

    private static List<TimePerformanceEvent> _timePerformanceEvents = new ArrayList<>();
    private static List<MemoryPerformanceEvent> _memoryPerformanceEvents = new ArrayList<>();

    public Runner(RunnerBuilder runnerBuilder) {
        _poolSize = runnerBuilder._poolSize;
        _termithIndex = runnerBuilder._termithIndex;
        _bpmnDiagram = runnerBuilder._bpmnDiagram;
        _base = RunnerBuilder._base;
        _out = RunnerBuilder._out;
        _lang = RunnerBuilder._lang;
        _treeTaggerHome = RunnerBuilder._treeTaggerHome;
        _learningPath = RunnerBuilder._learningPath;
        _evaluationPath = RunnerBuilder._evaluationPath;
        _scorePath = RunnerBuilder._scorePath;
    }

    public static Path getOut() {
        return _out;
    }

    public static List<TimePerformanceEvent> getTimePerformanceEvents() {
        return _timePerformanceEvents;
    }

    public static List<MemoryPerformanceEvent> getMemoryPerformanceEvents() {
        return _memoryPerformanceEvents;
    }

    public static Path getBase() {return _base;}

    public static String getLang() {
        return _lang;
    }

    public static Path getEvaluationPath() {
        return _evaluationPath;
    }

    public static Path getLearningPath() {
        return _learningPath;
    }

    public static Path getScorePath() {
        return _scorePath;
    }

    public static String getTreeTaggerHome() {
        return _treeTaggerHome;
    }

    /**
     * this method contains the process chain. This method calls inherited delegate classes.
     * @throws IOException Throws an IO exception if a file is not found or have a permission problem during process
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void execute() throws IOException, InterruptedException {

        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = cfg.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(_bpmnDiagram)
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        runtimeService.startProcessInstanceByKey(processDefinition.getKey());

    }
}
