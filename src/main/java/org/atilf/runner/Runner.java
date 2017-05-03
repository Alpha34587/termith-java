package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.impl.variable.CustomObjectType;
import org.flowable.engine.impl.variable.VariableType;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to create some Delegate inherited classes and execute each of them linearly.
 * @author Simon Meoni Created on 10/11/16.
 */
public class Runner {

    private String _bpmnDiagram;


    private HashMap<String,Object> _flowableVariable = new HashMap<>();
    private List<VariableType> _variableTypes = new ArrayList<>();

    public Runner(RunnerBuilder runnerBuilder) {
        _bpmnDiagram = runnerBuilder._bpmnDiagram;
        _flowableVariable.put("poolSize", runnerBuilder._poolSize);
        _flowableVariable.put("termithIndex", runnerBuilder._termithIndex);
        _flowableVariable.put("thresholdMin", runnerBuilder._thresholdMin);
        _flowableVariable.put("thresholdMax", runnerBuilder._thresholdMax);
        _flowableVariable.put("txmInputPath", runnerBuilder._txmInputPath);
        _flowableVariable.put("base", runnerBuilder._base);
        _flowableVariable.put("out", runnerBuilder._out);
        _flowableVariable.put("lang", runnerBuilder._lang);
        _flowableVariable.put("treeTaggerHome", runnerBuilder._treeTaggerHome);
        _flowableVariable.put("learningPath", runnerBuilder._learningPath);
        _flowableVariable.put("evaluationPath", runnerBuilder._evaluationPath);
        _flowableVariable.put("corpusSize", runnerBuilder._corpusSize);
        _flowableVariable.put("annotation", runnerBuilder._annotation);
        _flowableVariable.put("timePerformanceEvents",new ArrayList<>());
        _flowableVariable.put("memoryPerformanceEvents",new ArrayList<>());

    }


    /**
     * this method contains the process chain. This method calls inherited delegate classes.
     * @throws IOException Throws an IO exception if a file is not found or have a permission problem during process
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void execute(){

        initVariableType();
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setCustomPreVariableTypes(_variableTypes)
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
        runtimeService.startProcessInstanceByKey(processDefinition.getKey(),_flowableVariable);

    }

    private void initVariableType() {
        _variableTypes.add(new CustomObjectType("TermithIndex",TermithIndex.class.getClass()));
        _variableTypes.add(new CustomObjectType("Path",Path.class.getClass()));
        _variableTypes.add(new CustomObjectType("ArrayList",List.class.getClass()));
    }
}
