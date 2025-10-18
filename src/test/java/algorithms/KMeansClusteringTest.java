package algorithms;

import org.junit.jupiter.api.Test;

import distanceFunctions.EuclideanDistance;
import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.NumericObject;
import result.Cluster;
import result.ClusterSet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class KMeansClusteringTest {

    @Test
    public void testBasicKMeans() {
        NumericObject obj1 = new NumericObject(1, new Double[]{1.0, 1.0}, "obj1");
        NumericObject obj2 = new NumericObject(2, new Double[]{1.1, 1.0}, "obj2");
        NumericObject obj3 = new NumericObject(3, new Double[]{5.0, 5.0}, "obj3");
        NumericObject obj4 = new NumericObject(4, new Double[]{5.1, 5.0}, "obj4");

        DataCollection collection = new DataCollection();
        collection.add(obj1);
        collection.add(obj2);
        collection.add(obj3);
        collection.add(obj4);

        IDistanceFunction distance = new EuclideanDistance();
        KMeansClustering kmeans = new KMeansClustering();
        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");

        ClusterSet clusters = kmeans.computeClusterSet(collection, distance, params);

        assertEquals(2, clusters.size(), "Should produce 2 clusters");
        int totalMembers = clusters.getClusters().stream().mapToInt(Cluster::size).sum();
        assertEquals(4, totalMembers, "All points assigned to clusters");
    }
}
