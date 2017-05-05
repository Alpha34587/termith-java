package org.atilf.module.tools;

import org.apache.commons.io.FileUtils;
import org.atilf.module.Module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * clean working directory after processing
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class WorkingFilesCleaner extends Module{
    private final Path _outputPath;
    private final boolean _keepFiles;
    private Path _learningPath = null;
    private Path _evaluationPath = null;
    private Path _txmInputPath = null;

    /**
     * constructor for WorkingFilesCleaner module
     *  @param outputPath the path of the working directory
     * @param learningPath
     * @param keepFiles if this value is true all subfolder of the directory is kept
     */
    public WorkingFilesCleaner(Path outputPath, Path learningPath, Path evaluationPath,
                               Path txmInputPath, boolean keepFiles) {
        _outputPath = outputPath;
        _evaluationPath = evaluationPath;
        _txmInputPath = txmInputPath;
        _keepFiles = keepFiles;
        _learningPath = learningPath;
    }

    /**
     * remove subfolder working directory if _keepFiles is false
     */
    @Override
    public void execute() {
        try {
            _logger.debug("clean working directory : {}",_outputPath);
            cleanOutputDirectory();
            cleanTxmInputDirectory();
            cleanLearningDirectory();
        } catch (IOException e) {
            _logger.error("no such file or directory : ", e);
        }
    }

    private void cleanLearningDirectory() throws IOException {
        if (_learningPath != null && _learningPath != _evaluationPath){
            Files.list(_learningPath).forEach(f -> {
                        _logger.info("delete " + f.getFileName() + " of learning corpus");
                        try {
                            Files.delete(Paths.get(_outputPath + "/" + f.getFileName()));
                        } catch (IOException e) {
                            _logger.error("cannot delete files",e);
                        }
                    }
            );
        }
    }

    private void cleanTxmInputDirectory() throws IOException {
        if (_txmInputPath != null){
            Files.list(_txmInputPath).forEach(f -> {
                        try {
                            _logger.info("delete " + f.getFileName() + " of learning corpus");
                            Files.delete(Paths.get(_outputPath + "/" + f.getFileName()));
                        } catch (IOException e) {
                            _logger.error("cannot delete files",e);
                        }
                    }
            );
        }
    }

    private void cleanOutputDirectory() throws IOException {
        Files.list(_outputPath).forEach(
                path -> {
                    File file = new File(path.toString());
                    /*
                    if _keepFiles is true and file is directory the file is kept
                     */
                    if (_keepFiles && file.isDirectory())
                        _logger.info("keeping " + file.getAbsolutePath() + " directory");

                    /*
                    the directory is deleted if _keepFiles is false
                     */
                    else if (file.isDirectory()){
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException e) {
                            _logger.error("cannot delete directory : ",e);
                        }
                    }
                    /*
                    remove serialized java object
                     */
                    else if (!file.getName().matches(".*(\\.xml|\\.json|\\.tbx|\\.csv)")) {
                            try {
                                Files.delete(file.toPath());
                            } catch (NoSuchFileException e) {
                                _logger.error("no such file or directory : ", e);
                            } catch (IOException e) {
                                _logger.error("File permission problem : ", e);
                            }
                        }
                }
        );
    }

    /**
     * call execute method
     */
    @Override
    public void run() {
        execute();
    }
}
