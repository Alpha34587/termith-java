package org.atilf.models.enrichment;

import org.junit.Assert;
import org.junit.Test;

public class SpecialChXmlEscapeTest {
    private String replacedString = "&<&>><'&\"";
    private String expectedString = "&amp;&lt;&amp;&gt;&gt;&lt;&apos;&amp;&quot;";
    @Test
    public void replaceCharTest() throws Exception {
        Assert.assertEquals("these string must be equals",
                expectedString,
                SpecialChXmlEscape.replaceChar(replacedString)
        );
    }

}