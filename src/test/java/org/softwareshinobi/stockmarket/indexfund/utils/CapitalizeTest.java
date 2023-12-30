package org.softwareshinobi.stockmarket.indexfund.utils;

import digital.softwareshinobi.napkinexchange.indexfund.utils.Capitalize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapitalizeTest {

    @Test
    void stringIsCapitalized() {
        assertEquals("String", Capitalize.capitalize("string"));
    }

    @Test
    void onlyFirstLetterIsCapitalized() {
        assertEquals("String", Capitalize.capitalize("String"));
        assertEquals("String", Capitalize.capitalize("STRing"));
        assertEquals("String", Capitalize.capitalize("sTrInG"));
    }

}