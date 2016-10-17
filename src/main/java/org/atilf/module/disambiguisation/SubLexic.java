package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexic {
    String lexOn;
    private Path p;
    private Map<String, Multiset> subLexics;

    public SubLexic(Path p, Map<String, Multiset> subLexics) {
        this.p = p;
        this.subLexics = subLexics;
    }

    public void execute() {

    }
}
