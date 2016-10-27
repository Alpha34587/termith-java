package org.atilf.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Simon Meoni
 *         Created on 08/09/16.
 */
public class TagNormalizer {

    protected final static Map<String, String> FR_TT_TAG;

    static {
        FR_TT_TAG = new ConcurrentHashMap<>();
        FR_TT_TAG.put("ABK", "abbreviation");
        FR_TT_TAG.put("ABR", "abbreviation");
        FR_TT_TAG.put("ADJ", "adjective");
        FR_TT_TAG.put("ADV", "adverb");
        FR_TT_TAG.put("DET:ART", "article");
        FR_TT_TAG.put("DET:POS", "determiner");
        FR_TT_TAG.put("INT", "interjection");
        FR_TT_TAG.put("KON", "conjunction");
        FR_TT_TAG.put("NAM", "name");
        FR_TT_TAG.put("NOM", "noun");
        FR_TT_TAG.put("NUM", "numeral");
        FR_TT_TAG.put("PRO", "pronoun");
        FR_TT_TAG.put("PRO:DEM", "pronoun");
        FR_TT_TAG.put("PRO:IND", "pronoun");
        FR_TT_TAG.put("PRO:PER", "pronoun");
        FR_TT_TAG.put("PRO:POS", "pronoun");
        FR_TT_TAG.put("PRO:REL", "pronoun");
        FR_TT_TAG.put("PRP", "adposition");
        FR_TT_TAG.put("PRP:det", "adposition");
        FR_TT_TAG.put("PUN", "punctuation");
        FR_TT_TAG.put("PUN:cit", "punctuation");
        FR_TT_TAG.put("SENT", "punctuation");
        FR_TT_TAG.put("SYM", "symbol");
        FR_TT_TAG.put("VER:cond", "verb");
        FR_TT_TAG.put("VER:futu", "verb");
        FR_TT_TAG.put("VER:impe", "verb");
        FR_TT_TAG.put("VER:impf", "verb");
        FR_TT_TAG.put("VER:infi", "verb");
        FR_TT_TAG.put("VER:pper", "verb");
        FR_TT_TAG.put("VER:ppre", "verb");
        FR_TT_TAG.put("VER:pres", "verb");
        FR_TT_TAG.put("VER:simp", "verb");
        FR_TT_TAG.put("VER:subi", "verb");
        FR_TT_TAG.put("VER:subp", "verb");
        FR_TT_TAG.put("VER:cond", "conditional");
        FR_TT_TAG.put("VER:futu", "indicative");
        FR_TT_TAG.put("VER:impe", "imperative");
        FR_TT_TAG.put("VER:impf", "indicative");
        FR_TT_TAG.put("VER:infi", "infinitive");
        FR_TT_TAG.put("VER:pper", "participle");
        FR_TT_TAG.put("VER:ppre", "participle");
        FR_TT_TAG.put("VER:pres", "indicative");
        FR_TT_TAG.put("VER:simp", "indicative");
        FR_TT_TAG.put("VER:subi", "subjunctive");
        FR_TT_TAG.put("VER:subp", "subjunctive");
        FR_TT_TAG.put("DET:POS", "possessive");
        FR_TT_TAG.put("PRO:DEM", "demonstrative");
        FR_TT_TAG.put("PRO:PER", "personal");
        FR_TT_TAG.put("PRO:IND", "indefinite");
        FR_TT_TAG.put("PRO:POS", "possessive");
        FR_TT_TAG.put("PRO:REL", "relative");
        FR_TT_TAG.put("PRP", "preposition");
        FR_TT_TAG.put("PRP:det", "preposition");
        FR_TT_TAG.put("PUN:cit", "citation");
        FR_TT_TAG.put("SENT", "sentence");
        FR_TT_TAG.put("VER:cond", "present");
        FR_TT_TAG.put("VER:futu", "future");
        FR_TT_TAG.put("VER:impe", "present");
        FR_TT_TAG.put("VER:impf", "imperfect");
        FR_TT_TAG.put("VER:infi", "present");
        FR_TT_TAG.put("VER:pper", "past");
        FR_TT_TAG.put("VER:ppre", "present");
        FR_TT_TAG.put("VER:pres", "present");
        FR_TT_TAG.put("VER:simp", "past");
        FR_TT_TAG.put("VER:subi", "imperfect");
        FR_TT_TAG.put("VER:subp", "present");
    }

    protected final static Map<String, String> EN_TT_TAG;

    static {
        EN_TT_TAG = new ConcurrentHashMap<>();
        EN_TT_TAG.put("CC", "conjunction");
        EN_TT_TAG.put("CD", "number");
        EN_TT_TAG.put("DT", "determiner");
        EN_TT_TAG.put("EX", "unknown");
        EN_TT_TAG.put("FW", "noun");
        EN_TT_TAG.put("IN", "adposition");
        EN_TT_TAG.put("IN/that", "adposition");
        EN_TT_TAG.put("JJ", "adjective");
        EN_TT_TAG.put("JJR", "adjective");
        EN_TT_TAG.put("JJS", "adjective");
        EN_TT_TAG.put("LS", "unknown");
        EN_TT_TAG.put("MD", "unknown");
        EN_TT_TAG.put("NN", "noun");
        EN_TT_TAG.put("NNS", "noun");
        EN_TT_TAG.put("NP", "name");
        EN_TT_TAG.put("NPS", "name");
        EN_TT_TAG.put("PDT", "determiner");
        EN_TT_TAG.put("POS", "determiner");
        EN_TT_TAG.put("PP", "pronoun");
        EN_TT_TAG.put("PP$", "pronoun");
        EN_TT_TAG.put("RB", "adverb");
        EN_TT_TAG.put("RBR", "adverb");
        EN_TT_TAG.put("RBS", "adverb");
        EN_TT_TAG.put("RP", "article");
        EN_TT_TAG.put("SENT", "sentence");
        EN_TT_TAG.put("SYM", "symbol");
        EN_TT_TAG.put("TO", "adposition");
        EN_TT_TAG.put("UH", "interjection");
        EN_TT_TAG.put("VB", "verb");
        EN_TT_TAG.put("VBD", "verb");
        EN_TT_TAG.put("VBG", "verb");
        EN_TT_TAG.put("VBN", "verb");
        EN_TT_TAG.put("VBP", "verb");
        EN_TT_TAG.put("VBZ", "verb");
        EN_TT_TAG.put("WDT", "determiner");
        EN_TT_TAG.put("WP", "pronoun");
        EN_TT_TAG.put("WP$", "pronoun");
        EN_TT_TAG.put("WRB", "adverb");
        EN_TT_TAG.put("VH", "verb");
        EN_TT_TAG.put("VHD", "verb");
        EN_TT_TAG.put("VHG", "verb");
        EN_TT_TAG.put("VHN", "verb");
        EN_TT_TAG.put("VHP", "verb");
        EN_TT_TAG.put("VHZ", "verb");
        EN_TT_TAG.put("VV", "verb");
        EN_TT_TAG.put("VVD", "verb");
        EN_TT_TAG.put("VVG", "verb");
        EN_TT_TAG.put("VVN", "verb");
        EN_TT_TAG.put("VVP", "verb");
        EN_TT_TAG.put("VVZ", "verb");
        EN_TT_TAG.put("``", "punctuation");
        EN_TT_TAG.put(",", "punctuation");
        EN_TT_TAG.put(":", "punctuation");
        EN_TT_TAG.put("''", "punctuation");
        EN_TT_TAG.put("(", "punctuation");
        EN_TT_TAG.put(")", "punctuation");
        EN_TT_TAG.put("$", "punctuation");
        EN_TT_TAG.put("#", "punctuation");
        EN_TT_TAG.put("IN", "preposition");
        EN_TT_TAG.put("IN/that", "preposition");
        EN_TT_TAG.put("TO", "preposition");
    }

    protected static Map<String,String> _ttTag;

    public static void initTag(String lang){
        switch (lang) {
            case "en" :
                _ttTag = EN_TT_TAG;
                break;
            case "fr" :
                _ttTag = FR_TT_TAG;
                break;
            default:
                break;
        }
    }

    public static String normalize(String token) {
        return _ttTag.get(token);
    }
}
