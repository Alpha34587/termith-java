package org.atilf.module.tools;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.SpecialCharacter;
import org.atilf.module.Module;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SpecialCharacterTranslator extends Module {
    private final File _file;
    private final TermithIndex _termithIndex;

    public SpecialCharacterTranslator(Path p, TermithIndex termithIndex) {
        _file = new File(p.toString());
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
                fw.write(SpecialCharacter.replaceChar(br.readLine()) + "\n");
            }

            fw.close();
            br.close();
            fr.close();
            Files.move(newFile.toPath(), _file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            _logger.error("could not write or read file",e);
        }
    }
}
