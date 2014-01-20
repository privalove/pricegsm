<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//div[@class='b-offers  b-offers__offers']"/>
    </xsl:template>

    <xsl:template match="//div[@class='b-offers  b-offers__offers']">
        <offer>
            <price>
                <xsl:value-of select="replace(.//span[@class='b-prices__num']/text(), '[^0-9]' ,'')"/>
            </price>
            <xsl:variable name="fullName" select="lower-case(.//h3[@class='b-offers__title']/a/text())"/>
            <name>
                <xsl:value-of select="$fullName"/>
            </name>

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
                <shopId>
                    <xsl:value-of select="replace(ul[@class='b-model-actioins']/li[2]/a/@href,'.*fesh=', '')"/>
                </shopId>
    </xsl:template>
</xsl:stylesheet>

