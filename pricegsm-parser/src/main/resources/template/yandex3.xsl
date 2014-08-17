<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <result>
        <xsl:apply-templates select="//div[@class='b-serp__item b-offers ']"/>
        <xsl:apply-templates select="//p[@class='search-stat']"/>
        </result>
    </xsl:template>

    <xsl:template match="//div[@class='b-serp__item b-offers ']">
        <offer>
            <price>
                <xsl:value-of select="replace(.//span[@class='b-old-prices__num']/text(), '[^0-9]' ,'')"/>
            </price>

            <xsl:apply-templates select=".//div[@class='b-offers__feats']"/>

        </offer>
    </xsl:template>

    <xsl:template match=".//div[@class='b-offers__feats']">
                <link>
                    <xsl:value-of select="a/@href"/>
                </link>
                <shop>
                    <xsl:value-of select="a/text()"/>
                </shop>
    </xsl:template>
    <xsl:template match="//p[@class='search-stat']">
        <xsl:variable name="stat" select="text()[1]"/>
        <xsl:analyze-string select="$stat"
                            regex=".*[^0-9]+([0-9]+)\.[^0-9]*$">
            <xsl:matching-substring>
            <count>
                <xsl:value-of select="regex-group(1)"/>
            </count>
            </xsl:matching-substring>
        </xsl:analyze-string>
    </xsl:template>

</xsl:stylesheet>

