package tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class TeiImporter {
    CopyOnWriteArrayList<File> corpus;

    public TeiImporter(String path) throws IOException {
        corpus = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    public List getCorpus() {
        return corpus;
    }

    public void setCorpus(CopyOnWriteArrayList corpus) {
        this.corpus = corpus;
    }
}
