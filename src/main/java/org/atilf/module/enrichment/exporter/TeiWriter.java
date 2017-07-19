package org.atilf.module.enrichment.exporter;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.*;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;
import org.atilf.resources.enrichment.StandOffResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.atilf.models.enrichment.SpecialChXmlEscape.replaceXmlChar;

/**
 * export result to tei/standOff annotation
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter extends Module{

    private Path _outputPath;
    private BufferedWriter _bufferedWriter = null;
    private StringBuilder _tokenizeBody;
    private StandOffResources _standOffRessources;
    private final StringBuilder _xmlCorpus;
    private final List<MorphologyOffsetId> _morphologyOffsetIds;
    private final List<MultiWordsOffsetId> _multiWordsOffsetIds;
    private final List<MultiWordsOffsetId> _resourceProjectorOffsetIds;
    private final List<MultiWordsOffsetId> _transdisciplinaryOffsetIds;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());


    /**
     * constructor for TeiWriter
     * @param key the concerned file
     * @param termithIndex the termithIndex of a process
     * @param standOffResources static resource used by teiWriter
     */
    public TeiWriter(String key,TermithIndex termithIndex, StandOffResources standOffResources, String outputhPath){
        /*
         * read externals object and file related to the key and call the other constructor
         */
        this(
                //xml corpus
                FilesUtils.readFile(termithIndex.getXmlCorpus().get(key)),
                //morphologyOffsetIds
                FilesUtils.readListObject(
                        termithIndex.getMorphologyStandOff().get(key)),
                //the tokenize body
                FilesUtils.readObject(termithIndex.getTokenizeTeiBody().get(key),StringBuilder.class),
                //the terminology
                termithIndex.getTerminologyStandOff().get(key),
                termithIndex.getPhraseoOffsetId().get(key),
                termithIndex.getTransdisciplinaryOffsetId().get(key),
                //the write output path
                Paths.get(outputhPath + "/" + key + ".xml"),
                standOffResources
        );
        try {

            //delete java objects files
            Files.delete(termithIndex.getMorphologyStandOff().get(key));
            Files.delete(termithIndex.getTokenizeTeiBody().get(key));
        } catch (IOException e) {
            LOGGER.error("cannot delete file",e);
        }

        _outputPath = Paths.get(outputhPath + "/" + key + ".xml");
        _termithIndex = termithIndex;
    }

    /**
     * constructor for teiWriter
     * @param xmlCorpus the xmlFile
     * @param morphologyOffsetIds morphology tags
     * @param tokenizeBody the tokenize body
     * @param multiWordsOffsetIds term entries tags
     * @param outputPath the output path
     * @param standOffResources static resource used by teiWriter
     */
    private TeiWriter(StringBuilder xmlCorpus,
                      List<MorphologyOffsetId> morphologyOffsetIds,
                      StringBuilder tokenizeBody, List<MultiWordsOffsetId> multiWordsOffsetIds,
                      List<MultiWordsOffsetId> resourceProjectorOffsetIds,
                      List<MultiWordsOffsetId> transdisciplinaryOffsetIds,
                      Path outputPath,
                      StandOffResources standOffResources) {

        _xmlCorpus = xmlCorpus;
        _morphologyOffsetIds = morphologyOffsetIds;
        _tokenizeBody = tokenizeBody;
        _multiWordsOffsetIds = multiWordsOffsetIds;
        _transdisciplinaryOffsetIds = transdisciplinaryOffsetIds;
        _resourceProjectorOffsetIds = resourceProjectorOffsetIds;
        _standOffRessources = standOffResources;
        try {
            _bufferedWriter = Files.newBufferedWriter(outputPath);
        } catch (IOException e) {
            LOGGER.error("cannot initialize buffered writer object",e);
        }

    }

    /**
     * write the new file. Insert new standOff tags and tokenize body
     */
    @Override
    public void execute() {
        LOGGER.debug("writing : {}",_outputPath);
        try {
            //insert standoff namespace
            insertStandoffNs();
            //insert standoff element
            insertStandOff();
            //insert tokenize body
            insertBody();
            _bufferedWriter.close();
        } catch (Exception e) {
            LOGGER.error("could not write file", e);
        }
    }

    /**
     * insert standoff namespace
     */
    private void insertStandoffNs(){
        int teiTag = _xmlCorpus.indexOf("<TEI ") + 5;
        _xmlCorpus.insert(teiTag, _standOffRessources.NS.substring(0, _standOffRessources.NS.length() - 1) + " ");
    }

    /**
     * insert tokenize body
     * @throws IOException thrown an exception if _bufferedWriter fields throws an error during writing
     */
    private void insertBody() throws IOException {
        _bufferedWriter.append(_tokenizeBody.append("\n"));
        _bufferedWriter.append(_xmlCorpus.subSequence(_xmlCorpus.indexOf("</text>") + 7  , _xmlCorpus.length()));
        _bufferedWriter.flush();
    }

    /**
     * search the beginning of the <text> element
     * @return the begin index
     */
    private int searchStart() {
        int index = _xmlCorpus.indexOf("<text>");
        if (index == -1){
            index = _xmlCorpus.indexOf("<text ");
        }
        return index;
    }

    /**
     * insert morphology and terminology standOff
     * @throws IOException thrown an exception if _bufferedWriter fields throws an error during writing
     */
    private void insertStandOff() throws IOException {
        /*
         * retrieve index of the text element
         */
        int startText = searchStart();
        _bufferedWriter.append(_xmlCorpus.subSequence(0,startText));
        /*
        inject morphology
         */
        if (_morphologyOffsetIds != null){
            serializeMorphosyntax(_morphologyOffsetIds);
        }

        /*
        inject terminology
         */
        if (_multiWordsOffsetIds != null &&
                !_multiWordsOffsetIds.isEmpty()){
            serializeTerminology(_multiWordsOffsetIds);
        }

        /*
        inject phraseo & lst
         */
        if (_resourceProjectorOffsetIds != null &&
                !_resourceProjectorOffsetIds.isEmpty()){
            serializePhraseology(_resourceProjectorOffsetIds);
        }
        if (_transdisciplinaryOffsetIds != null &&
                !_transdisciplinaryOffsetIds.isEmpty()){
            serializeTransdisciplinary(_transdisciplinaryOffsetIds);
        }



    }

    private void serializeTransdisciplinary(List<MultiWordsOffsetId> transdisciplinaryOffsetIds) throws IOException {
        serializeOffsetId(transdisciplinaryOffsetIds,"lexiquesTransdisciplinaires",
                _standOffRessources.LST_SPAN,
                _standOffRessources.LST_TEI_HEADER);
    }

    private void serializePhraseology(List<MultiWordsOffsetId> resourceProjectorOffsetIds) throws IOException {
        serializeOffsetId(resourceProjectorOffsetIds,"syntagmesDefinis", _standOffRessources.PH_SPAN, _standOffRessources.PH_TEI_HEADER);
    }

    /**
     * this method convert a list of MultiWordsOffsetId into a standoff element
     * @param multiWordsOffsetIds the MultiWordsOffsetId list
     * @throws IOException thrown an exception if _bufferedWriter fields throws an error during writing
     */
    private void serializeTerminology(List<MultiWordsOffsetId> multiWordsOffsetIds) throws IOException {
        serializeOffsetId(multiWordsOffsetIds,"candidatsTermes", _standOffRessources.T_SPAN, _standOffRessources.T_TEI_HEADER);
    }

    private void serializeOffsetId(List<? extends MultiWordsOffsetId> termOffsetIds, String type, StringBuilder
            spanTemplate, StringBuilder teiHeaderTemplate) throws
            IOException {
    /*
    reorder the list
     */
        termOffsetIds.sort((o1, o2) -> {
            int comp = o1.getIds().get(0).compareTo(o2.getIds().get(0));
            if (comp == 0) {
                comp = ((Integer) o1.getIds().size()).compareTo(o2.getIds().size()) * -1;
            }
            return comp;
        });

        /*
        write the standoff element root
         */
        _bufferedWriter.append(replaceTemplate(cut(new StringBuilder(_standOffRessources.STANDOFF),false),"@type",type));
        _bufferedWriter.append(teiHeaderTemplate);
        if (type.equals("candidatsTermes")) {
            _bufferedWriter.append(_standOffRessources.T_INTERP_GRP);
        }
        _bufferedWriter.append(cut(_standOffRessources.LIST_ANNOTATION, false));
        /*
        write his content
         */
        for (MultiWordsOffsetId token : termOffsetIds) {
            /*
            write a span element
             */
            StringBuilder entry = new StringBuilder(spanTemplate);
            replaceTemplate(entry,"@target", serializeId(token.getIds()));
            replaceTemplate(entry, "@corresp", String.valueOf(token.getTermId()));
            replaceTemplate(entry, "@string", replaceXmlChar(token.getWord()));
            _bufferedWriter.append(entry);
        }
        /*
        write end elements
         */
        _bufferedWriter.append(cut(_standOffRessources.LIST_ANNOTATION,true));
        _bufferedWriter.append(cut(_standOffRessources.STANDOFF,true));
    }

    /**
     * return the start tag or the close tag of a resource from _standOffRessources
     * @return the start tag or close tag
     */
    private StringBuilder cut(StringBuilder template,boolean closedTag) {
        String separator = "(?<=\n)";
        if (closedTag)
            return new StringBuilder(template.toString().split(separator)[1]);
        else
            return new StringBuilder(template.toString().split(separator)[0]);
    }

    /**
     * modify standoff element resource
     * @param template the standoff template
     * @param model the resource to replace
     * @param occurence the element who replaced the model variable
     * @return the modified template
     */
    private StringBuilder replaceTemplate(StringBuilder template, String model, String occurence) {
        int index = template.indexOf(model);
        template.replace(index, index + model.length(), occurence);
        return template;
    }

    /**
     * convert MorphologyOffsetId into standoff element
     * @param morphologyOffsetIds the list of morphologyOffsetId
     * @throws IOException thrown an exception if _bufferedWriter fields throws an error during writing
     */
    private void serializeMorphosyntax(List<MorphologyOffsetId> morphologyOffsetIds) throws IOException {
        /*
        write standOff element root
         */
        _bufferedWriter.append(replaceTemplate(cut(_standOffRessources.STANDOFF,false),"@type","wordForms"));
        _bufferedWriter.append(_standOffRessources.MS_TEI_HEADER);
        _bufferedWriter.append(cut(_standOffRessources.LIST_ANNOTATION,false));
        /*
        write content of standOff element
         */
        for (MorphologyOffsetId token : morphologyOffsetIds) {
            /*
            write span element
             */
            StringBuilder entry = new StringBuilder(_standOffRessources.MS_SPAN);
            replaceTemplate(entry, "@target", serializeId(token.getIds()));
            replaceTemplate(entry, "@lemma", replaceXmlChar(token.getLemma().replace("<unknown>", "@unknown")));
            replaceTemplate(entry, "@pos", token.getTag());
            _bufferedWriter.append(entry);
        }
        _bufferedWriter.append(cut(_standOffRessources.LIST_ANNOTATION,true));
        _bufferedWriter.append(cut(_standOffRessources.STANDOFF,true));
    }

    /**
     * convert ids to xml targets
     * @param ids the id list
     * @return the xml target
     */
    private String serializeId(List<Integer> ids) {
        StringBuilder target = new StringBuilder();
        for (int id : ids) {
            target.append("#t").append(id).append(" ");
        }
        return target.substring(0,target.length() - 1);
    }

    /**
     * call execute method
     */
    @Override
    public void run() {
        super.run();
        _termithIndex.getOutputFile().add(_outputPath);
    }
}
