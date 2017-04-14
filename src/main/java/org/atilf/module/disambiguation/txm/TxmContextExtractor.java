package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.TxmContext;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmContextExtractor extends ContextExtractor {


    private final HashMap<String, List<TxmContext>> _observedTxmContexts;

    public TxmContextExtractor(String p, HashMap<String, List<TxmContext>> observedTxmContexts) {
        super(p, null, null);
        _observedTxmContexts = observedTxmContexts;
    }
}
