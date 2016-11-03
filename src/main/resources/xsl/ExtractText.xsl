<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tei="http://www.tei-c.org/ns/1.0" version="2.0">
  <xsl:output method="text" encoding="UTF-8"/>
  <xsl:template match="/">
    <xsl:apply-templates select="//tei:text"/>
  </xsl:template>
  <xsl:template match="*">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="text()[ancestor::tei:p|ancestor::tei:head|ancestor::tei:div|ancestor::tei:q|ancestor::tei:ab|ancestor::tei:note][position() = last()]">
    <xsl:value-of select="."/>
    <xsl:if test="not(normalize-space(.) = '')">
      <xsl:text>&#10;</xsl:text>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
