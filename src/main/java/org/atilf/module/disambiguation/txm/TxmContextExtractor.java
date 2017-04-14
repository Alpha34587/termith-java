package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.TxmContext;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import java.util.*;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmContextExtractor extends ContextExtractor {


    private Map<String, List<TxmContext>> _txmContexts = new HashMap<>();
    private String _annotation;
    private static final Logger LOGGER = LoggerFactory.getLogger(TxmContextExtractor.class.getName());

    public TxmContextExtractor(String p, Map<String, List<TxmContext>> txmContexts, String annotation) {
        super(p, null, null);
        _txmContexts = txmContexts;
        _annotation = annotation;
    }


    @Override
    protected void extractTerms(Attributes attributes) {
        String ana = attributes.getValue("ana");
        if (ana.equals(_annotation)) {
            _terms.add(new ContextTerm(attributes.getValue("corresp"),
                    ana,
                    attributes.getValue("target")));
            LOGGER.debug("term extracted: " + attributes.getValue("corresp"));
        }
    }

    @Override
    protected void addContextToLexicon(String key, Map<Integer, String> contextTarget) {
        if (!_txmContexts.containsKey(key)) {
            _txmContexts.put(key, new ArrayList<>());
        }
        TxmContext context = new TxmContext();
        SortedMap<Integer,String> contextTree = new TreeMap<>(contextTarget);
        contextTree.forEach(
                (k,v) -> {
                    String[] vSplit = v.split(" ");
                    context.addElements(k.toString(),vSplit[0],vSplit[1],vSplit[2]);
                }
        );

        context.setFilename(_p);
        _txmContexts.get(key).add(context);
    }

    @Override
    protected String normalizeKey(String c, String l) {
        return (c + "_" + l).replace("#", "");
    }
}
