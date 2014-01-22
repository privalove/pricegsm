<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//div[@class='b-offers ']"/>
    </xsl:template>

    <xsl:template match="//div[@class='b-offers ']">
        <offer>
            <price>
                <xsl:value-of select="replace(.//span[@class='b-prices__num']/text(), '[^0-9]' ,'')"/>
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
</xsl:stylesheet>

