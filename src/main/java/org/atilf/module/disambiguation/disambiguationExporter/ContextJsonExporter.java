package org.atilf.module.disambiguation.disambiguationExporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.atilf.models.TermithIndex;
import org.atilf.module.Module;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Simon Meoni Created on 07/02/17.
 */
public class ContextJsonExporter extends Module{
    private final Path _exportPath;
    String _p;

    public ContextJsonExporter(TermithIndex termithIndex, Path exportPath) {
        super(termithIndex);
        _exportPath = exportPath;
    }

    @Override
    protected void execute() {
        try{
            _logger.info("exportation to json is started");
            JsonFactory f=new JsonFactory();
            JsonGenerator g=f.createGenerator(
                    new FileWriter(_exportPath + "/termith-context.json")
            );
            g.useDefaultPrettyPrinter();
            g.writeStartObject();
            g.writeArrayFieldStart("corpus");
            _termithIndex.getCorpusLexicon().getLexicalTable().elementSet().forEach(
                    key -> {

                        try {
                            g.writeStartObject();
                            g.writeNumberField(key,_termithIndex.getCorpusLexicon().getLexicalTable().count(key));
                            g.writeEndObject();
                        } catch (IOException e) {
                            _logger.error("cannot write corpus occurrence",e);
                        }

                    }
            );
            g.writeEndArray();
            g.writeArrayFieldStart("terms");
            _termithIndex.getContextLexicon().forEach(
                    (key,value)->{
                        try{
                            g.writeStartObject();
                            g.writeStringField("key",key);
                            g.writeObjectFieldStart("occurrences");
                            value.getLexicalTable().elementSet().forEach(
                                    occ -> {
                                        try {
                                            g.writeNumberField(occ, value.getLexicalTable().count(occ));
                                        } catch (IOException e) {
                                            _logger.error("cannot write occurrence ",e);
                                        }
                                    }
                            );
                            g.writeEndObject();
                            g.writeEndObject();
                        }catch(IOException e){
                            _logger.error("cannot write context : ",e);
                        }
                    }
            );
            g.writeEndArray();
            g.writeEndObject();
            g.close();
            _logger.info("exportation to json is finished");
        }
        catch(IOException e){
            _logger.error("cannot export in JSON",e);

        }
    }
}
