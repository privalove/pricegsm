package com.pricegsm.parser;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 13:27
 */
public class ProductNameSearcherTest extends TestCase {
    @Test
    public void testIsCellFind() {
        // Given
        ProductNameSearcher productNameSearcher = new ProductNameSearcher("iPhone 5S 16Gb");
        // When
        // Then
        assertEquals(true, productNameSearcher.isCellFind("iPhone 5S 16Gb"));
        assertEquals(true, productNameSearcher.isCellFind("16Gb iPhone 5S"));
        assertEquals(true, productNameSearcher.isCellFind("5S 16Gb iPhone"));

        assertEquals(true, productNameSearcher.isCellFind("iphone 5s 16gb"));
        assertEquals(true, productNameSearcher.isCellFind("16gb iphone 5s"));
        assertEquals(true, productNameSearcher.isCellFind("5s 16gb iphone"));

        assertEquals(true, productNameSearcher.isCellFind("IPHONE 5s 16GB"));
        assertEquals(true, productNameSearcher.isCellFind("16GB iphone 5S"));
        assertEquals(true, productNameSearcher.isCellFind("5S 16gb IPHONE"));

        assertEquals(false, productNameSearcher.isCellFind("iPhone 5C 16Gb"));
        assertEquals(false, productNameSearcher.isCellFind("16Gb iPhone 5C"));
        assertEquals(false, productNameSearcher.isCellFind("5C 16Gb iPhone"));

        assertEquals(false, productNameSearcher.isCellFind("iphone 5c 16gb"));
        assertEquals(false, productNameSearcher.isCellFind("16gb iphone 5c"));
        assertEquals(false, productNameSearcher.isCellFind("5c 16gb iphone"));

        assertEquals(false, productNameSearcher.isCellFind("IPHONE 5c 16GB"));
        assertEquals(false, productNameSearcher.isCellFind("16GB iphone 5C"));
        assertEquals(false, productNameSearcher.isCellFind("5C 16gb IPHONE"));
    }

    public void testCommaSeparator() throws Exception {
        ProductNameSearcher productNameSearcher =
                new ProductNameSearcher("iPhone 5S 16, iPhone 5S 16gb");

        assertEquals(true, productNameSearcher.isCellFind("iPhone 5S 16Gb"));
        assertEquals(true, productNameSearcher.isCellFind("16Gb iPhone 5S"));
        assertEquals(true, productNameSearcher.isCellFind("5S 16Gb iPhone"));

        assertEquals(true, productNameSearcher.isCellFind("iphone 5s 16gb"));
        assertEquals(true, productNameSearcher.isCellFind("16gb iphone 5s"));
        assertEquals(true, productNameSearcher.isCellFind("5s 16gb iphone"));

        assertEquals(true, productNameSearcher.isCellFind("IPHONE 5s 16GB"));
        assertEquals(true, productNameSearcher.isCellFind("16GB iphone 5S"));
        assertEquals(true, productNameSearcher.isCellFind("5S 16gb IPHONE"));

        assertEquals(false, productNameSearcher.isCellFind("iPhone 5C 16Gb"));
        assertEquals(false, productNameSearcher.isCellFind("16Gb iPhone 5C"));
        assertEquals(false, productNameSearcher.isCellFind("5C 16Gb iPhone"));
    }

    public void testGB() throws Exception {
        ProductNameSearcher productNameSearcher =
                new ProductNameSearcher("iPhone 5S 16, iPhone 5S 16GB");

        assertEquals(true, productNameSearcher.isCellFind("iPhone 5S 16Gb"));

        assertEquals(false, productNameSearcher.isCellFind("5C 16gb IPHONE"));

    }

    public void testRealCase() throws Exception {
        ProductNameSearcher productNameSearcher = new ProductNameSearcher(
                "ipad mini 3 16 cell,ipad mini 3 16 cellular,ipad mini 3 16 4g");

        assertEquals(true, productNameSearcher.isCellFind("iPad mini 3 4G 16GB "));
    }

    public void testAddedWords() throws Exception {

        ProductNameSearcher productNameSearcher = new ProductNameSearcher(
                "iPhone 6 16gb");

        assertEquals(true, productNameSearcher.isCellFind("iPhone 6 16gb"));
        assertEquals(false, productNameSearcher.isCellFind("iPhone 6 plus 16gb"));
    }

    public void testEmpty() throws Exception {

        ProductNameSearcher productNameSearcher = new ProductNameSearcher("");

        assertEquals(false, productNameSearcher.isCellFind(""));
    }
}
