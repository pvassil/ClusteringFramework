package distanceFunctions;

import org.junit.jupiter.api.Test;

import dom.StringObject;

import static org.junit.jupiter.api.Assertions.*;

public class DTWDistanceTest {

    @Test
    public void testDTWSimpleSequences() {
        StringObject s1 = new StringObject(1, "abc", "seq1");
        StringObject s2 = new StringObject(2, "abc", "seq2");
        StringObject s3 = new StringObject(3, "abd", "seq3");
        StringObject s4 = new StringObject(4, "abcd", "seq4");

        DTWDistance dtw = new DTWDistance();

        // Identical sequences -> distance = 0
        assertEquals(0.0, dtw.computeDistance(s1, s2), 1e-6);

        // Small difference -> distance > 0
        double dist_s1_s3 = dtw.computeDistance(s1, s3);
        assertTrue(dist_s1_s3 > 0);

        // Different lengths
        double dist_s1_s4 = dtw.computeDistance(s1, s4);
        assertTrue(dist_s1_s4 > 0);

        // Symmetry
        assertEquals(dtw.computeDistance(s1, s4), dtw.computeDistance(s4, s1), 1e-6);
    }

    @Test
    public void testDTWWithCustomCosts() {
        StringObject s1 = new StringObject(1, "ab", "seq1");
        StringObject s2 = new StringObject(2, "ac", "seq2");

        DTWDistance dtw = new DTWDistance(2.0, 2.0, 1.5);

        double dist = dtw.computeDistance(s1, s2);
        assertTrue(dist > 0, "Distance should be positive with custom costs");
    }
}
