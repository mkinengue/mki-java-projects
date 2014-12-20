<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:variable name="newline">
		<xsl:text>
		</xsl:text>
	</xsl:variable>
	<xsl:strip-space elements="*" />

	<xsl:template match="/">
		<xsl:text>|| Input WDI || Mapping with SBE || Rules || Sprint ||</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="text()">
		<xsl:text>| </xsl:text>
		<xsl:for-each select="ancestor-or-self::*">
			<xsl:text>/</xsl:text>
			<xsl:value-of select="name()" />
		</xsl:for-each>
		<xsl:text> |  |  |  |</xsl:text>
		<xsl:value-of select="$newline" />
	</xsl:template>

</xsl:stylesheet>