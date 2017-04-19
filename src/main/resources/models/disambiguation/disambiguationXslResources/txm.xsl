<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns:ns="http://standoff.proposal"
                version="2.0">

    <xsl:output method="xml" encoding="UTF-8"/>
    <xsl:key match="//ns:standOff[@type = 'wordForms']/ns:listAnnotation/tei:span" name="spanKey" use="@target"/>

    <xsl:template match="/">
        <xsl:apply-templates select="*"/>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@* | processing-instruction() | comment()">
        <xsl:copy/>
    </xsl:template>

    <xsl:template match="ns:listAnnotation">
        <xsl:for-each select="tei:span">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="ns:standOff/@type"/>
    <xsl:template match="ns:standOff"/>

    <xsl:template match="ns:standOff[@type = 'candidatsTermes']">
        <xsl:copy>
            <xsl:apply-templates select="ns:listAnnotation"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="tei:w">
        <xsl:copy>
            <xsl:attribute name="xml:id">
                <xsl:value-of select="@xml:id"/>
            </xsl:attribute>
            <xsl:variable name="var" select="key('spanKey',concat('#',@xml:id))"/>
            <xsl:value-of select="$var//tei:f[@name = 'lemma']/tei:string/text()"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="$var//tei:f[@name = 'pos']/tei:symbol/@value"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="text()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="ns:standOff[@type = 'wordForms']"/>
    <xsl:template match="tei:teiHeader"/>
    <xsl:template match="tei:span//*"/>
    <xsl:template match="tei:span">
        <xsl:copy>
            <xsl:copy-of select="@*[not(name(.) = 'ana')]"/>
            <xsl:choose>
                <xsl:when test="contains(@ana, ' ')">
                    <xsl:attribute name="ana">
                        <xsl:value-of select="substring-before(@ana,' ')"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="@ana"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:attribute name="text">
                <xsl:value-of select=".//tei:string"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="tei:span/@*">

    </xsl:template>

    <xsl:template match="tei:span//text()"/>
    <xsl:template match="tei:interpGrp"/>
</xsl:stylesheet>
