package com.pricegsm.parser;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 12:53
 */
public class PriceSearcherTest extends TestCase {
    @Test
    public void testIsFind() {
        // Given
        // When
        // Then
        PriceSearcher searcher = new PriceSearcher();
        assertEquals(true, searcher.isCellFind("123.5"));
        assertEquals(true, searcher.isCellFind("123.5"));
        assertEquals(false, searcher.isCellFind("123.5ddd"));
    }
}
