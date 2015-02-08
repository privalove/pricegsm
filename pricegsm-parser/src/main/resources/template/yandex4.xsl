<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <result>
            <xsl:apply-templates select="search-result/search-item/offer"/>
            <xsl:apply-templates select="search-result"/>
        </result>
    </xsl:template>

    <xsl:template match="offer">
        <offer>
            <xsl:apply-templates select="price"/>
            <xsl:apply-templates select="shop"/>
            <xsl:apply-templates select="url"/>
        </offer>
    </xsl:template>

    <xsl:template match="price">
        <price>
            <xsl:apply-templates/>
        </price>
    </xsl:template>

    <xsl:template match="shop">
        <xsl:apply-templates select="name"/>
    </xsl:template>

    <xsl:template match="name">
        <shop>
            <xsl:apply-templates/>
        </shop>
    </xsl:template>

    <xsl:template match="url">
        <link>
            <xsl:apply-templates/>
        </link>
    </xsl:template>


    <xsl:template match="search-result">
        <count>
            <xsl:value-of select="//search-result/@total"/>
        </count>
    </xsl:template>

</xsl:stylesheet>

