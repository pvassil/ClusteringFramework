package algorithms;

import org.junit.jupiter.api.Test;

import distanceFunctions.EditDistance;
import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.StringObject;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceMatrixTest {

    @Test
    public void testDistanceMatrixComputation() {
        StringObject obj1 = new StringObject(1, "abc", "obj1");
        StringObject obj2 = new StringObject(2, "abd", "obj2");
        StringObject obj3 = new StringObject(3, "abcd", "obj3");

        DataCollection collection = new DataCollection();
        collection.add(obj1);
        collection.add(obj2);
        collection.add(obj3);

        IDistanceFunction distance = new EditDistance();
        DistanceMatrix matrix = new DistanceMatrix(collection, distance);
        matrix.compute();

        assertEquals(0.0, matrix.getDistance(obj1, obj1), 1e-6);
        assertEquals(1.0, matrix.getDistance(obj1, obj2), 1e-6);
        assertEquals(1.0, matrix.getDistance(obj2, obj1), 1e-6); // symmetry
        assertEquals(1.0, matrix.getDistance(obj2, obj3), 1e-6);
        assertEquals(1.0, matrix.getDistance(obj1, obj3), 1e-6); // abc -> abcd
    }

    @Test
    public void testPrintMatrix() {
        StringObject obj1 = new StringObject(1, "a", "obj1");
        StringObject obj2 = new StringObject(2, "b", "obj2");

        DataCollection collection = new DataCollection();
        collection.add(obj1);
        collection.add(obj2);

        IDistanceFunction distance = new EditDistance();
        DistanceMatrix matrix = new DistanceMatrix(collection, distance);

        // Should compute and print without exception
        matrix.printMatrix();
    }
}
