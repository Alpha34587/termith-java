package module.termsuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Stack;

/**
 * @author Simon Meoni
 *         Created on 25/08/16.
 */
class JsonReaderMorphoSyntax implements JsonReader {
    private static Logger LOGGER = LoggerFactory.getLogger(JsonReaderMorphoSyntax.class);
    private Stack<Token> tokenStack;

    public JsonReaderMorphoSyntax(){
        this.tokenStack = new Stack<>();
    }

    public JsonReaderMorphoSyntax(Stack<Token> tokenStack){
        this.tokenStack = tokenStack;
    }

    Stack<Token> getTokenStack() {
        return tokenStack;
    }

    @Override
    public void parsing(File file) {

    }

    public void clean(){}

    static class Token {
        public String pos;
        public String lemma;
        public int begin;
        public int end;

        Token(String pos, String lemma, int begin, int end) {
            this.pos = pos;
            this.lemma = lemma;
            this.begin = begin;
            this.end = end;
        }
    }
}
