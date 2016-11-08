package org.atilf.module.tools;

import org.atilf.models.tei.exporter.StandOffResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.models.termsuite.MorphologyOffsetId;
import org.atilf.models.termsuite.TermsOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.atilf.models.tei.exporter.SpecialChXmlEscape.replaceXmlChar;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter implements Runnable {

    private Path _outputPath;
    private BufferedWriter _bufferedWriter = null;
    private StringBuilder _tokenizeBody;
    private StandOffResources _stdfRes;
    private TermithIndex _termithIndex;
    private final StringBuilder _xmlCorpus;
    private final List<MorphologyOffsetId> _morphologyOffsetIds;
    private final List<TermsOffsetId> _termsOffsetIds;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());


    public TeiWriter(String key,TermithIndex termithIndex, StandOffResources standOffResources){
        this(
                FilesUtils.readFile(termithIndex.getXmlCorpus().get(key)),
                FilesUtils.readListObject(
                        termithIndex.getMorphologyStandOff().get(key),
                        MorphologyOffsetId.class),
                FilesUtils.readObject(termithIndex.getTokenizeTeiBody().get(key),StringBuilder.class),
                termithIndex.getTerminologyStandOff().get(key),
                Paths.get(TermithIndex.getOutputPath() + "/" + key + ".xml"),
                standOffResources
        );
        try {
            Files.delete(termithIndex.getMorphologyStandOff().get(key));
            Files.delete(termithIndex.getTokenizeTeiBody().get(key));
        } catch (IOException e) {
            LOGGER.error("cannot delete file",e);
        }
        _outputPath = Paths.get(TermithIndex.getOutputPath() + "/" + key + ".xml");
        _termithIndex = termithIndex;
    }
    public TeiWriter(StringBuilder xmlCorpus,
                     List<MorphologyOffsetId> morphologyOffsetIds,
                     StringBuilder tokenizeBody,List<TermsOffsetId> termsOffsetIds,
                     Path outputPath,
                     StandOffResources standOffResources) {

        _xmlCorpus = xmlCorpus;
        _morphologyOffsetIds = morphologyOffsetIds;
        _tokenizeBody = tokenizeBody;
        _termsOffsetIds = termsOffsetIds;
        _stdfRes = standOffResources;
        try {
            _bufferedWriter = Files.newBufferedWriter(outputPath);
        } catch (IOException e) {
            LOGGER.error("cannot initialize buffered writer object",e);
        }

    }


    public void execute() throws IOException {
        try {
            insertStandoffNs();
            insertStandOff();
            insertBody();
            _bufferedWriter.close();
        }
        catch (Exception e){
            LOGGER.error("could not write file",e);
        }
    }

    private void insertStandoffNs() throws IOException {
        int teiTag = _xmlCorpus.indexOf("<TEI ") + 5;
        _xmlCorpus.insert(teiTag, _stdfRes.NS.substring(0, _stdfRes.NS.length() - 1) + " ");
    }

    private void insertBody() throws IOException {
        _bufferedWriter.append(_tokenizeBody.append("\n"));
        _bufferedWriter.append(_xmlCorpus.subSequence(_xmlCorpus.indexOf("</text>") + 7  , _xmlCorpus.length()));
        _bufferedWriter.flush();
    }

    private int searchStart() {
        int index = _xmlCorpus.indexOf("<text>");
        if (index == -1){
            index = _xmlCorpus.indexOf("<text ");
        }
        return index;
    }

    private void insertStandOff() throws IOException {
        int startText = searchStart();
        _bufferedWriter.append(_xmlCorpus.subSequence(0,startText));
        if (_morphologyOffsetIds != null){
            serializeMorphosyntax(_morphologyOffsetIds);
        }

        if (_termsOffsetIds != null &&
                !_termsOffsetIds.isEmpty()){
            serializeTerminology(_termsOffsetIds);
        }

    }

    private void serializeTerminology(List<TermsOffsetId> termsOffsetIds) throws IOException {
        termsOffsetIds.sort((o1, o2) -> {
            int comp = o1.getIds().get(0).compareTo(o2.getIds().get(0));
            if (comp == 0) {
                comp = ((Integer) o1.getIds().size()).compareTo(o2.getIds().size()) * -1;
            }
            return comp;
        });
        _bufferedWriter.append(replaceTemplate(cut(new StringBuilder(_stdfRes.STANDOFF),false),"@type","candidatsTermes"));
        _bufferedWriter.append(_stdfRes.T_TEI_HEADER);
        _bufferedWriter.append(cut(_stdfRes.LIST_ANNOTATION,false));
        for (TermsOffsetId token : termsOffsetIds) {
            StringBuilder entry = new StringBuilder(_stdfRes.T_SPAN);
            replaceTemplate(entry,"@target", serializeId(token.getIds()));
            replaceTemplate(entry, "@corresp", String.valueOf(token.getTermId()));
            replaceTemplate(entry, "@string", replaceXmlChar(token.getWord()));
            _bufferedWriter.append(entry);
        }
        _bufferedWriter.append(cut(_stdfRes.LIST_ANNOTATION,true));
        _bufferedWriter.append(cut(_stdfRes.STANDOFF,true));
    }

    private StringBuilder cut(StringBuilder template,boolean closedTag) {
        String separator = "(?<=\n)";
        if (closedTag)
            return new StringBuilder(template.toString().split(separator)[1]);
        else
            return new StringBuilder(template.toString().split(separator)[0]);
    }

    private StringBuilder replaceTemplate(StringBuilder template, String model, String occurence) {
        int index = template.indexOf(model);
        template.replace(index, index + model.length(), occurence);
        return template;
    }

    private void serializeMorphosyntax(List<MorphologyOffsetId> morphologyOffsetIds) throws IOException {
        _bufferedWriter.append(replaceTemplate(cut(_stdfRes.STANDOFF,false),"@type","wordForms"));
        _bufferedWriter.append(_stdfRes.MS_TEI_HEADER);
        _bufferedWriter.append(cut(_stdfRes.LIST_ANNOTATION,false));
        for (MorphologyOffsetId token : morphologyOffsetIds) {
            StringBuilder entry = new StringBuilder(_stdfRes.MS_SPAN);
            replaceTemplate(entry, "@target", serializeId(token.getIds()));
            replaceTemplate(entry, "@lemma", replaceXmlChar(token.getLemma().replace("<unknown>", "@unknown")));
            replaceTemplate(entry, "@pos", token.getTag());
            _bufferedWriter.append(entry);
        }
        _bufferedWriter.append(cut(_stdfRes.LIST_ANNOTATION,true));
        _bufferedWriter.append(cut(_stdfRes.STANDOFF,true));
    }

    private String serializeId(List<Integer> ids) {
        String target = "";
        for (int id : ids) {
            target += "#t" + id + " ";
        }
        return target.substring(0,target.length() - 1);
    }

    @Override
    public void run() {
        LOGGER.debug("writing : " + _outputPath);
        try {
            this.execute();
            _termithIndex.getOutputFile().add(_outputPath);
        } catch (IOException e) {
            LOGGER.error("errors during writing xml/tei file",e);
        }
    }
}
