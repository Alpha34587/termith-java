package module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 06/09/16.
 */
public class TextAnalyzer {

    int documentSize;

    int nbOfDocs;
    int begin;
    int end;
    int docIndex;
    int cumulSize;
    int totalSize;
    boolean isLastDoc;
    public TextAnalyzer(){}

    public TextAnalyzer(int documentSize,
                        int nbOfDocs,
                        int end,
                        int docIndex,
                        int cumulSize,
                        int totalSize,
                        boolean isLastDoc) {
        this.documentSize = documentSize;
        this.nbOfDocs = nbOfDocs;
        this.begin = 0;
        this.end = end;
        this.docIndex = docIndex;
        this.cumulSize = cumulSize;
        this.totalSize = totalSize;
        this.isLastDoc = isLastDoc;
    }





}
