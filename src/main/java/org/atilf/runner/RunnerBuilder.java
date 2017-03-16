package org.atilf.runner;

import org.atilf.models.TermithIndex;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunnerBuilder {

    TermithIndex _termithIndex;
    String _bpmnDiagram;
    int _poolSize = Runtime.getRuntime().availableProcessors();

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

    public RunnerBuilder setBase(String _base) {
        RunnerBuilder._base = Paths.get(_base);
        return this;
    }

    public RunnerBuilder setOut(String _out) {
        RunnerBuilder._out = Paths.get(_out);
        return this;
    }

    public RunnerBuilder setLang(String _lang) {
        RunnerBuilder._lang = _lang;
        return this;
    }

    public RunnerBuilder setTreeTaggerHome(String _treeTaggerHome) {
        RunnerBuilder._treeTaggerHome = _treeTaggerHome;
        return this;
    }

    public RunnerBuilder setLearningPath(String _learningPath) {
        RunnerBuilder._learningPath = Paths.get(_learningPath);
        return this;
    }

    public RunnerBuilder setEvaluationPath(String _evaluationPath) {
        RunnerBuilder._evaluationPath = Paths.get(_evaluationPath);
        return this;
    }

    public RunnerBuilder setScorePath(String _scorePath) {
        RunnerBuilder._scorePath = Paths.get(_scorePath);
        return this;
    }

    public Runner createRunner() {
        return new Runner(this);
    }
}
