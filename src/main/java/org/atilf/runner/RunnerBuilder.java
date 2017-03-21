package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.atilf.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunnerBuilder {

    int _poolSize = Runtime.getRuntime().availableProcessors();
    TermithIndex _termithIndex = new TermithIndex();
    String _bpmnDiagram;

    /*
    remove maybe
     */
    static int _corpusSize;

    /*
    common parameter
     */

    static Path _base;
    static Path _out;

    /*
    terminology extraction parameter
     */
    static String _lang;
    static String _treeTaggerHome;

    /*
    Disambiguation parameter
     */
    static Path _learningPath;
    static Path _evaluationPath;
    static Path _scorePath;
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public RunnerBuilder setTermithIndex(TermithIndex termithIndex) {
        _termithIndex = termithIndex;
        return this;
    }

    public RunnerBuilder setPoolSize(int poolSize) {
        _poolSize = poolSize;
        return this;
    }

    public RunnerBuilder setBpmnDiagram(String bpmnDiagram) {
        _bpmnDiagram = bpmnDiagram;
        return this;
    }

    public RunnerBuilder setBase(String _base){
        RunnerBuilder._base = FilesUtils.folderPathResolver(_base);
        try {
            RunnerBuilder._corpusSize = (int) Files.list(Paths.get(_base)).count();
        } catch (IOException e) {
            LOGGER.error("cannot find corpus : ",e);
        }
        return this;
    }

    public RunnerBuilder setOut(String _out) {
        RunnerBuilder._out = Paths.get(_out);
        FilesUtils.createFolder(RunnerBuilder._out);
        return this;
    }

    public RunnerBuilder setLang(String _lang) {
        RunnerBuilder._lang = _lang;
        return this;
    }

    public RunnerBuilder setTreeTaggerHome(String treeTaggerHome) {
        RunnerBuilder._treeTaggerHome = treeTaggerHome;
        return this;
    }

    public RunnerBuilder setLearningPath(String learningPath) {
        RunnerBuilder._learningPath = FilesUtils.folderPathResolver(learningPath);
        return this;
    }

    public RunnerBuilder setEvaluationPath(String evaluationPath) {
        RunnerBuilder._evaluationPath = FilesUtils.folderPathResolver(evaluationPath);
        return this;
    }

    public RunnerBuilder setScorePath(String scorePath) {
        RunnerBuilder._scorePath = FilesUtils.folderPathResolver(scorePath);
        FilesUtils.createFolder(RunnerBuilder._scorePath);
        return this;
    }

    public Runner createRunner() {
        return new Runner(this);
    }

}
