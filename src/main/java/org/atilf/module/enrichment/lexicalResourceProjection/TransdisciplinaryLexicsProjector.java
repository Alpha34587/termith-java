package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.lexicalResourceProjectionResources;

import java.util.HashMap;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class TransdisciplinaryLexicsProjector extends PhraseologyProjector {
    public TransdisciplinaryLexicsProjector(String id, TermithIndex termithIndex, lexicalResourceProjectionResources transdisciplinaryResource) {
        super(id, termithIndex, transdisciplinaryResource);
    }

    public void SyntitaDetection(HashMap _fixedMap,
                                 HashMap _variousMap,
                                 HashMap _normaliseeInitialeMap) {
//        int variableLength = 3;
//        String detectFixedLemma = "";
//        String detectVariableLemma = "";
//        Set<String> variableSyntitas;
//        for (List<TagWithId> sentence : this.sentences) {
//            int limitOfSentence = sentence.size() + 1;
//
//            for (int wordIndex = 0; wordIndex < limitOfSentence; wordIndex++) {
//
//                while (length <= 8) {
//
//                    int wordAfter = wordIndex + length;
//                    if (wordAfter < limitOfSentence) {
//                        List<TagWithId> splitSentence = sentence.subList(wordIndex, wordAfter);
//                        /*
//                        This portion of code is used to detect the fixed lemma
//                         */
//                        for (TagWithId detectWord : splitSentence) {
//                            detectFixedLemma += detectWord.lemma + " ";
//                        }
//                        /*
//                        This portion of code is used to detect the various lemma
//                         */
//                        if (wordAfter + variableLength < limitOfSentence) {
//                            List<TagWithId> splitVariableSentence = sentence
//                                    .subList(wordIndex + 1, wordAfter + variableLength);
//
//                            for (TagWithId detectWord : splitVariableSentence) {
//                                detectVariableLemma += detectWord.lemma + " ";
//                            }
//                            detectVariableLemma = PrepareData.eraseLastSpace(detectVariableLemma);
//
//                            variableSyntitas = VariableSyntitaGenerate(detectVariableLemma, variableLength, 1);
//
//                            /*
//                            We check if the entry exists on the variable hashmap and we add the syntagme
//                             */
//                            for (String variableSyntita : variableSyntitas) {
//                                if (_variousMap.containsKey(variableSyntita))
//                                    addProbSyntita(splitVariableSentence,
//                                            _normaliseeInitialeMap, _variousMap, variableSyntita);
//                            }
//
//                        }
//
//                        /*
//                        We check if the entry exists on the fixed hashmap and we add the syntagme
//                        */
//                        detectFixedLemma = PrepareData.eraseLastSpace(detectFixedLemma);
//                        if (_fixedMap.containsKey(detectFixedLemma))
//                            addProbSyntita(splitSentence,
//                                    _normaliseeInitialeMap, _fixedMap, detectFixedLemma);
//                        detectFixedLemma = "";
//                        detectVariableLemma = "";
//                        length++;
//                    } else
//                        break;
//                }
//                length = 2;
//            }
//        }
    }

}
