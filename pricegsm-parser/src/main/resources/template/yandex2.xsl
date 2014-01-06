<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"/>
    <xsl:param name="colorPattern"/>
    <xsl:param name="position"/>
    <xsl:template match="/">
        <xsl:apply-templates select="//div[@class='b-offers__list']"/>
    </xsl:template>

    <xsl:template match="//div[@class='b-offers__list']">
        <xsl:for-each select=".//div[@class='b-offers  b-offers__offers']">
            <offer>
                <position>
                    <xsl:value-of select="$position"/>
                </position>
                <price>
                    <xsl:value-of select="replace(.//span[@class='b-prices__num']/text(), '[^0-9]' ,'')"/>
                </price>
                <xsl:variable name="fullName" select="lower-case(.//h3[@class='b-offers__title']/a/text())"/>
                <name>
                    <xsl:value-of select="$fullName"/>
                </name>
                <color>
                    <xsl:analyze-string select="$fullName" regex="{$colorPattern}">
                        <xsl:matching-substring>
                            <xsl:value-of select="regex-group(1)"/>
                            <xsl:value-of select="regex-group(2)"/>
                            <xsl:value-of select="regex-group(3)"/>
                            <xsl:value-of select="regex-group(4)"/>
                            <xsl:value-of select="regex-group(5)"/>
                            <xsl:value-of select="regex-group(6)"/>
                        </xsl:matching-substring>
                    </xsl:analyze-string>
                </color>
                <link>
                    <xsl:value-of select=".//div[@class='b-offers__feats']/a/@href"/>
                </link>
                <shop>
                    <xsl:value-of select=".//div[@class='b-offers__feats']/a/text()"/>
                </shop>
            </offer>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>

