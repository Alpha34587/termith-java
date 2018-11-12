package org.atilf.models.disambiguation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * this class is the converted contextLexicon or the CorpusLexicon into R format
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexicon {
    private int _size;
    private String _outputPath;
    private StringBuffer _rName = new StringBuffer();
    private StringBuffer _rOcc = new StringBuffer();
    private CorpusLexicon _corpus;
    private LexiconProfile _lexiconProfile;
    private List<String> _idContextLexicon;
    private Path _csvPath;
    private final static Logger LOGGER = LoggerFactory.getLogger(RLexicon.class);
    /**
     * constructor used to convert corpus into R variable
     * @param corpus
     */
    public RLexicon(CorpusLexicon corpus, String outputPath){
        _corpus = corpus;
        _size = corpus.lexicalSize();
        _outputPath = outputPath;
        _corpus.forEach(this::convertToRGlobal);
        writeFile();
    }

    /**
     * constructor used to convert a lexicon profile into R variable
     * @param lexiconProfile the lexicon profile
     * @param corpus the corpus
     */
    public RLexicon(LexiconProfile lexiconProfile, CorpusLexicon corpus, String outputPath) {
        _lexiconProfile = lexiconProfile;
        _corpus = corpus;
        _outputPath = outputPath;
        _idContextLexicon = new ArrayList<>();
        _size = lexiconProfile.lexicalSize();
        _lexiconProfile.forEach(this::convertToRContext);
        writeFile();
    }

    private void writeFile(){
        _csvPath = Paths.get(_outputPath + "/" + UUID.randomUUID().toString()).toAbsolutePath();
        File file = new File(_csvPath.toString());
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter))
        {
            _rName.deleteCharAt(_rName.length() - 1);
            _rOcc.deleteCharAt(_rOcc.length() - 1);
            bufferedWriter.append(_rName).append("\n");
            bufferedWriter.append(_rOcc);
        } catch (Exception e) {
            LOGGER.error("cannot write file : " + _rName + " " + _lexiconProfile.getLexicalTable().toString(), e);
        }
    }

    public Path getCsvPath() {
        return _csvPath;
    }


    /**
     * get the id of the _context Lexicon corresponds to the id of the CorpusLexicon class
     * @return the list of id of each words
     * @see CorpusLexicon
     */
    public List<String> getIdContextLexicon() {
        return _idContextLexicon;
    }

    /**
     * get the size of the _context or Corpus
     */
    public int getSize() { return _size; }

    /**
     * used to convert a corpus
     * @param word a word
     */
    private void convertToRGlobal(String word) {
        _rName.append(_corpus.getIdEntry(word)).append(",");
        _rOcc.append(_corpus.count(word)).append(",");
    }

    /**
     * used to convert a contextLexicon
     * @param word a word
     */
    private void convertToRContext(String word) {
        if (_corpus.getLexicalTable().contains(word)) {
            _rName.append(_corpus.getIdEntry(word)).append(",");
            _rOcc.append(_lexiconProfile.count(word)).append(",");
            _idContextLexicon.add(_corpus.getIdEntry(word));
        }
    }
}
