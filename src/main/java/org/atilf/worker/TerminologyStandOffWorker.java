package org.atilf.worker;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.TermithIndex;
import org.atilf.models.TermsOffsetId;
import org.atilf.module.termsuite.terminology.TerminologyStandOff;
import org.atilf.module.tools.FilesUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.NavigableMap;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TerminologyStandOffWorker implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminologyStandOffWorker.class.getName());
    private final String id;
    private final List<MorphoSyntaxOffsetId> morpho;
    private final List<TermsOffsetId> termino;
    private NavigableMap<Integer,List<Integer>> beginMap;
    private NavigableMap<Integer,List<Integer>> endMap;
    public TerminologyStandOffWorker(String id,
                                     Path morpho,
                                     TermithIndex termithIndex) {
        this.id = id;
        this.morpho = (List<MorphoSyntaxOffsetId>) FilesUtilities.readObject(morpho);
        this.termino = termithIndex.getTerminologyStandOff().get(id);
    }

    @Override
    public void run() {
        LOGGER.debug("retrieve morphosyntax id for file :" + id);
        TerminologyStandOff terminologyStandOff = new TerminologyStandOff(id,morpho,termino);
        terminologyStandOff.execute();
        LOGGER.debug("retrieve id task finished");
    }
}
