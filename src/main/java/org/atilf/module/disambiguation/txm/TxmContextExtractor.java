package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;

import java.util.List;
import java.util.Map;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmContextExtractor extends ContextExtractor {
    public TxmContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon) {
        super(p, contextLexicon, corpusLexicon);
    }

    public TxmContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon, int threshold, List<String> authorizedPOSTag) {
        super(p, contextLexicon, corpusLexicon, threshold, authorizedPOSTag);
    }

    public TxmContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon, int threshold, List<String> authorizedPOSTag, List<String> allowedElements) {
        super(p, contextLexicon, corpusLexicon, threshold, authorizedPOSTag, allowedElements);
    }
}
