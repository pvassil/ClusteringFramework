package distanceFunctions;

import org.junit.jupiter.api.Test;

import dom.StringObject;

import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {

    @Test
    public void testBasicLevenshtein() {
        StringObject obj1 = new StringObject(1, "kitten", "obj1");
        StringObject obj2 = new StringObject(2, "sitting", "obj2");

        IDistanceFunction distance = new EditDistance();
        double d = distance.computeDistance(obj1, obj2);

        assertEquals(3.0, d, "Levenshtein distance between 'kitten' and 'sitting' should be 3");
    }

    @Test
    public void testIdenticalStrings() {
        StringObject obj1 = new StringObject(1, "database", "obj1");
        StringObject obj2 = new StringObject(2, "database", "obj2");

        IDistanceFunction distance = new EditDistance();
        double d = distance.computeDistance(obj1, obj2);

        assertEquals(0.0, d, "Distance between identical strings should be 0");
    }

    @Test
    public void testEmptyStrings() {
        StringObject obj1 = new StringObject(1, "", "obj1");
        StringObject obj2 = new StringObject(2, "", "obj2");

        IDistanceFunction distance = new EditDistance();
        double d = distance.computeDistance(obj1, obj2);

        assertEquals(0.0, d, "Distance between two empty strings should be 0");
    }

    @Test
    public void testCustomCosts() {
        StringObject obj1 = new StringObject(1, "abc", "obj1");
        StringObject obj2 = new StringObject(2, "ac", "obj2");

        IDistanceFunction distance = new EditDistance(2.0, 2.0, 1.0); // insert=2, delete=2, replace=1
        double d = distance.computeDistance(obj1, obj2);

        // Deleting 'b' should cost 2
        assertEquals(2.0, d, "Distance with custom costs should match expected value 2.0");
    }

    @Test
    public void testSymmetry() {
        StringObject obj1 = new StringObject(1, "hello", "obj1");
        StringObject obj2 = new StringObject(2, "hallo", "obj2");

        IDistanceFunction distance = new EditDistance();
        double d1 = distance.computeDistance(obj1, obj2);
        double d2 = distance.computeDistance(obj2, obj1);

        assertEquals(d1, d2, "Distance should be symmetric");
    }
}
