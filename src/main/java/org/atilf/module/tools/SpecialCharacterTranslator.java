package org.atilf.module.tools;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.SpecialCharacter;
import org.atilf.module.Module;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SpecialCharacterTranslator extends Module {
    private final File _file;
    private final String _fileName;
    private final TermithIndex _termithIndex;

    public SpecialCharacterTranslator(String fileName, TermithIndex termithIndex) {
        _file = termithIndex.getXmlCorpus().get(fileName).toFile();
        _fileName = fileName;
        _termithIndex = termithIndex;
    }

    @Override
    protected void execute() {
        try {
            File newFile = new File(_file.toString() + ".new");
            FileWriter fw = new FileWriter(newFile);

            Reader fr = new FileReader(_file);
            BufferedReader br = new BufferedReader(fr);

            while(br.ready()) {
                fw.write(SpecialCharacter.replaceChar(br.readLine() + "\n"));
            }

            fw.close();
            br.close();
            fr.close();
            Files.move(newFile.toPath(), _file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            _logger.info("the task for file: {} is finished", _fileName);
        } catch (IOException e) {
            _logger.error("could not write or read file",e);
        }
    }
}
