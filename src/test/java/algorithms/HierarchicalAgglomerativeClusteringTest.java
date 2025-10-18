package algorithms;

import org.junit.jupiter.api.Test;

import distanceFunctions.EditDistance;
import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.StringObject;
import result.Cluster;
import result.ClusterSet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class HierarchicalAgglomerativeClusteringTest {

    @Test
    public void testDistanceComputation() {
        StringObject obj1 = new StringObject(1, "abc", "obj1");
        StringObject obj2 = new StringObject(2, "abd", "obj2");

        IDistanceFunction distance = new EditDistance();
        double d = distance.computeDistance(obj1, obj2);

        assertEquals(1.0, d, "Levenshtein distance between 'abc' and 'abd' should be 1");
    }

    @Test
    public void testSingleLinkageHAC() {
        StringObject obj1 = new StringObject(1, "a", "obj1");
        StringObject obj2 = new StringObject(2, "b", "obj2");
        StringObject obj3 = new StringObject(3, "c", "obj3");

        DataCollection collection = new DataCollection();
        collection.add(obj1);
        collection.add(obj2);
        collection.add(obj3);

        IDistanceFunction distance = new EditDistance();
        IClusteringAlgorithm hac = new HierarchicalAgglomerativeClustering();

        HashMap<String, String> params = new HashMap<>();
        params.put("targetClusters", "2");

        ClusterSet clusters = hac.computeClusterSet(collection, distance, params);

        assertEquals(2, clusters.size(), "Should produce exactly 2 clusters");

        // Check that all objects are assigned
        int totalMembers = clusters.getClusters().stream().mapToInt(Cluster::size).sum();
        assertEquals(3, totalMembers, "All objects should be included in clusters");
    }

    @Test
    public void testEmptyCollection() {
        DataCollection collection = new DataCollection();
        IDistanceFunction distance = new EditDistance();
        IClusteringAlgorithm hac = new HierarchicalAgglomerativeClustering();
        HashMap<String, String> params = new HashMap<>();

        ClusterSet clusters = hac.computeClusterSet(collection, distance, params);
        assertEquals(0, clusters.size(), "Empty collection should result in 0 clusters");
    }
}
