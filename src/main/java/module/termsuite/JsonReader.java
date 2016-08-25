package module.termsuite;

import java.io.File;

/**
 * This class is used to convert the Json files of termsuite into java object, there are two type of files who can generate by it :
 * 1) terminology.json : it is the terminology of the corpus
 * 2) json morphosyntax files : it is an analyze of morphosyntax of a single file of the corpus
 * @author Simon Meoni
 *         Created on 24/08/16.
 */
public interface JsonReader {

    public void parsing(File file);


}
