package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {
    @Test
    void testGetArgument() {
        var test1 = "TEST 1";
        var test2 = "TEST 23 45";
        var tes3 = "TEST 1 TEST";

        assertEquals(1, Utilities.getArgument(test1));
        assertEquals(23, Utilities.getArgument(test2));
        assertEquals(1, Utilities.getArgument(tes3));
    }

}