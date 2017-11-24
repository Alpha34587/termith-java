package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.atilf.runner.TermithResourceManager.*;

public class RunnerBuilder {

    String _annotation;
    int _poolSize = Runtime.getRuntime().availableProcessors();
    int _thresholdMax = 0;
    int _thresholdMin = 0;
    TermithIndex _termithIndex = new TermithIndex();
    String _bpmnDiagram;


    /*
    remove maybe
     */
    int _corpusSize = 0;

    /*
    common parameter
     */

    Path _base;
    Path _out;

    /*
    terminology extraction parameter
     */
    String _lang;
    String _treeTaggerHome;

    /*
    Disambiguation parameter
     */
    Path _learningPath;
    Path _evaluationPath;
    Path _txmInputPath;
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public RunnerBuilder setTermithIndex(TermithIndex termithIndex) {
        _termithIndex = termithIndex;
        return this;
    }

    public RunnerBuilder setAnnotation(String annotation) {
        _annotation = annotation;
        return this;
    }

    public RunnerBuilder setPoolSize(int poolSize) {
        _poolSize = poolSize;
        return this;
    }

    public RunnerBuilder setBpmnDiagram(String bpmnDiagramFilename) {
        _bpmnDiagram = TermithResource.CUSTOM_BPMN_DIAGRAM.getPath() + bpmnDiagramFilename;
        return this;
    }

    public RunnerBuilder setResourceManager(String resourceManager) throws Exception {
        TermithResource.setLang(_lang);
        addToClasspath(resourceManager);
        return this;
    }

    public RunnerBuilder setBase(String base){
        _base = FilesUtils.folderPathResolver(base);
        try {
            _corpusSize = (int) Files.list(Paths.get(base)).count();
        } catch (IOException e) {
            LOGGER.error("cannot find corpus : ",e);
        }
        return this;
    }

    public RunnerBuilder setTxmInputPath(String txmInputPath) {
        _txmInputPath = FilesUtils.folderPathResolver(txmInputPath);
        return this;
    }

    public RunnerBuilder setThresholds(int thresholdMin, int thresholdMax) {
        if (thresholdMin >= thresholdMax) {
            throw new IllegalArgumentException("the minimum threshold is superior or equals to the maximum threshold");
        }
        else if (thresholdMax < 0 || thresholdMin < 0){
            throw new IllegalArgumentException("one of these thresholds is negative");
        }
        _thresholdMin = thresholdMin;
        _thresholdMax = thresholdMax;
        return this;
    }

    public RunnerBuilder setOut(String out) {
        _out = Paths.get(out);
        FilesUtils.createFolder(_out);
        return this;
    }

    public RunnerBuilder setLang(String lang) {
        _lang = lang;
        return this;
    }

    public RunnerBuilder setTreeTaggerHome(String treeTaggerHome) {
        _treeTaggerHome = treeTaggerHome;
        return this;
    }

    public RunnerBuilder setLearningPath(String learningPath) {
        _learningPath = FilesUtils.folderPathResolver(learningPath);
        try {
            _corpusSize += (int) Files.list(_learningPath).count();
        } catch (IOException e) {
            LOGGER.error("cannot find path",e);
        }

        return this;
    }

    public RunnerBuilder setEvaluationPath(String evaluationPath) {
        _evaluationPath = FilesUtils.folderPathResolver(evaluationPath);
        try {
            _corpusSize += (int) Files.list(_evaluationPath).count();
        } catch (IOException e) {
            LOGGER.error("cannot find path",e);
        }
        return this;
    }

    public Runner createRunner() throws Exception {
        return new Runner(this);
    }
}
