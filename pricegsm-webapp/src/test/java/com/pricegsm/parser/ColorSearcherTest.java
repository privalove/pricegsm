package com.pricegsm.parser;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 13:11
 */
public class ColorSearcherTest extends TestCase {
    @Test
    public void testIsCellFind() {
        // Given
        ColorSearcher colorSearcher = new ColorSearcher("gold,золотoй");
        // When
        // Then
        assertEquals(true, colorSearcher.isCellFind("gold"));
        assertEquals(true, colorSearcher.isCellFind("золотoй"));
        assertEquals(true, colorSearcher.isCellFind("gold,золотoй"));
        assertEquals(true, colorSearcher.isCellFind("gold, золотoй"));
        assertEquals(true, colorSearcher.isCellFind("gold золотoй"));
        assertEquals(true, colorSearcher.isCellFind("золотoй,gold"));
        assertEquals(true, colorSearcher.isCellFind("золотoй, gold"));
        assertEquals(true, colorSearcher.isCellFind("золотoй gold"));

        assertEquals(true, colorSearcher.isCellFind("GOLD"));
        assertEquals(true, colorSearcher.isCellFind("ЗОЛОТOЙ"));
        assertEquals(true, colorSearcher.isCellFind("GOLD,ЗОЛОТOЙ"));
        assertEquals(true, colorSearcher.isCellFind("GOLD, ЗОЛОТOЙ"));
        assertEquals(true, colorSearcher.isCellFind("GOLD ЗОЛОТOЙ"));
        assertEquals(true, colorSearcher.isCellFind("ЗОЛОТOЙ,GOLD"));
        assertEquals(true, colorSearcher.isCellFind("ЗОЛОТOЙ, GOLD"));
        assertEquals(true, colorSearcher.isCellFind("ЗОЛОТOЙ GOLD"));

        assertEquals(false, colorSearcher.isCellFind("gray, серый"));
        assertEquals(false, colorSearcher.isCellFind("gray,серый"));
        assertEquals(false, colorSearcher.isCellFind("gray серый"));
        assertEquals(false, colorSearcher.isCellFind("серый"));
        assertEquals(false, colorSearcher.isCellFind("gray"));

        assertEquals(false, colorSearcher.isCellFind("GRAY, СЕРЫЙ"));
        assertEquals(false, colorSearcher.isCellFind("GRAY,СЕРЫЙ"));
        assertEquals(false, colorSearcher.isCellFind("GRAY СЕРЫЙ"));
        assertEquals(false, colorSearcher.isCellFind("СЕРЫЙ"));
        assertEquals(false, colorSearcher.isCellFind("GRAY"));
    }
}
