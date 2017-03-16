package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;

import java.io.IOException;

/**
 * This class is used to create some Delegate inherited classes and executeTasks each of them linearly.
 * @author Simon Meoni Created on 10/11/16.
 */
public class Runner {

    public static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static TermithIndex _termithIndex;
    public static int _poolSize;
    private String _bpmnDiagram;

    /**
     * this constructor initializes the _termithIndex field.
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    protected Runner(TermithIndex termithIndex) {
        this(termithIndex, DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initializes the _termithIndex field and the number of delegate used during the process
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of delegate used during the process
     */
    protected Runner(TermithIndex termithIndex, int poolSize) {
        _poolSize = poolSize;
        _termithIndex = termithIndex;
    }

    public Runner(TermithIndex termithIndex, String bpmnDiagram) {
        _poolSize = DEFAULT_POOL_SIZE;
        _termithIndex = termithIndex;
        _bpmnDiagram = bpmnDiagram;
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
                .addClasspathResource("runner/termithTreeTagger.bpmn20.xml")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey());
    }
}
