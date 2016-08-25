package module.termsuite;

import eu.project.ttc.readers.TermSuiteJsonCasDeserializer;

/**
 * This class is used to convert the Json files of termsuite into java object, there are two type of files who can generate by it :
 * 1) terminology.json : it is the terminology of the corpus
 * 2) json morphosyntax files : it is an analyze of morphosyntax of a single file of the corpus
 * @author Simon Meoni
 *         Created on 24/08/16.
 */
public abstract class JsonReader extends TermSuiteJsonCasDeserializer{

    public enum terminologyJsonAttribute {

    }

    public enum morphosyntaxJsonAttribute {

    }

    public void parsing(){

    }

    public class Token {
        public String pos;
        public String lemma;
        public int begin;
        public int end;

        public Token(String pos, String lemma, int begin, int end) {
            this.pos = pos;
            this.lemma = lemma;
            this.begin = begin;
            this.end = end;
        }
    }
}
