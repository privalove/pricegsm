<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <result>
            <error>0</error>
            <xsl:for-each select="//ul[@class='b-model-actioins']/li[2]/a">
                <link>
                    <xsl:value-of select="replace(@href,'.*fesh=', '')"/>
                </link>
            </xsl:for-each>
        </result>
    </xsl:template>
</xsl:stylesheet>

