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

public class DBSCANClusteringTest {

    @Test
    public void testBasicDBSCAN() {
        // Simple points for clustering
        StringObject obj1 = new StringObject(1, "aaa", "obj1");
        StringObject obj2 = new StringObject(2, "aab", "obj2");
        StringObject obj3 = new StringObject(3, "bbb", "obj3");
        StringObject obj4 = new StringObject(4, "bbc", "obj4");

        DataCollection collection = new DataCollection();
        collection.add(obj1);
        collection.add(obj2);
        collection.add(obj3);
        collection.add(obj4);

        IDistanceFunction distance = new EditDistance();

        DBSCANClustering dbscan = new DBSCANClustering();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("eps", "1.5"); // allow 1-2 character differences
        params.put("minPts", "2");

        ClusterSet clusterSet = dbscan.computeClusterSet(collection, distance, params);

        assertEquals(2, clusterSet.size(), "Should find 2 clusters");

        // Check cluster membership counts
        int totalMembers = clusterSet.getClusters().stream().mapToInt(Cluster::size).sum();
        assertEquals(4, totalMembers, "All points should belong to a cluster");

        // Print clusters for visual inspection
        System.out.println(clusterSet);
    }

    @Test
    public void testNoisePoints() {
        StringObject obj1 = new StringObject(1, "aaa", "obj1");
        StringObject obj2 = new StringObject(2, "xyz", "obj2"); // far away

        DataCollection collection = new DataCollection();
        collection.add(obj1);
        collection.add(obj2);

        IDistanceFunction distance = new EditDistance();
        DBSCANClustering dbscan = new DBSCANClustering();
        HashMap<String, String> params = new HashMap<>();
        params.put("eps", "1.0");
        params.put("minPts", "2");

        ClusterSet clusterSet = dbscan.computeClusterSet(collection, distance, params);

        // Only obj1 is alone -> no clusters expected
        assertEquals(0, clusterSet.size(), "No clusters should form; all points are noise");
    }
}
