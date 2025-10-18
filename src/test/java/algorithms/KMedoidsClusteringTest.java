package algorithms;

import org.junit.jupiter.api.Test;

import distanceFunctions.EditDistance;
import distanceFunctions.EuclideanDistance;
import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.NumericObject;
import dom.StringObject;
import result.Cluster;
import result.ClusterSet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class KMedoidsClusteringTest {

    @Test
    public void testKMedoidsOnStrings() {
        // Prepare string data
        StringObject s1 = new StringObject(1, "-(4)b(9)_(13)c(18)", "schema1");
        StringObject s2 = new StringObject(2, "-(4)b(9)_(14)c(20)", "schema2");
        StringObject s3 = new StringObject(3, "-(3)b(8)_(12)c(4)c(15)c(7)_(56)", "schema3");

        DataCollection stringData = new DataCollection();
        stringData.add(s1);
        stringData.add(s2);
        stringData.add(s3);

        IDistanceFunction editDist = new EditDistance();

        // KMedoids parameters
        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");
        params.put("randomSeed", "42");
        params.put("numRuns", "3");
        params.put("maxIterations", "50");

        KMedoidsClustering kmedoids = new KMedoidsClustering();
        ClusterSet clusters = kmedoids.computeClusterSet(stringData, editDist, params);
        System.out.println(clusters.toString());
        
        assertEquals(2, clusters.size(), "Should produce 2 clusters");
        int totalMembers = clusters.getClusters().stream().mapToInt(Cluster::size).sum();
        assertEquals(3, totalMembers, "All string objects should be assigned to a cluster");
    }

    @Test
    public void testKMedoidsOnNumeric() {
        // Prepare numeric data
        NumericObject n1 = new NumericObject(1, new Double[]{1.0, 2.0}, "vector1");
        NumericObject n2 = new NumericObject(2, new Double[]{2.0, 2.5}, "vector2");
        NumericObject n3 = new NumericObject(3, new Double[]{10.0, 10.0}, "vector3");

        DataCollection numericData = new DataCollection();
        numericData.add(n1);
        numericData.add(n2);
        numericData.add(n3);

        IDistanceFunction euclidean = new EuclideanDistance();

        // KMedoids parameters
        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");
        params.put("randomSeed", "123");
        params.put("numRuns", "3");
        params.put("maxIterations", "50");

        KMedoidsClustering kmedoids = new KMedoidsClustering();
        ClusterSet clusters = kmedoids.computeClusterSet(numericData, euclidean, params);
        System.out.println(clusters.toString());

        assertEquals(2, clusters.size(), "Should produce 2 clusters");
        int totalMembers = clusters.getClusters().stream().mapToInt(Cluster::size).sum();
        assertEquals(3, totalMembers, "All numeric objects should be assigned to a cluster");
    }

    @Test
    public void testReproducibility() {
        // Using same data and random seed, results should be identical
        NumericObject n1 = new NumericObject(1, new Double[]{1.0, 2.0}, "vector1");
        NumericObject n2 = new NumericObject(2, new Double[]{2.0, 2.5}, "vector2");
        NumericObject n3 = new NumericObject(3, new Double[]{10.0, 10.0}, "vector3");

        DataCollection numericData = new DataCollection();
        numericData.add(n1);
        numericData.add(n2);
        numericData.add(n3);

        IDistanceFunction euclidean = new EuclideanDistance();

        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");
        params.put("randomSeed", "999");
        params.put("numRuns", "1");
        params.put("maxIterations", "50");

        KMedoidsClustering kmedoids = new KMedoidsClustering();
        ClusterSet firstRun = kmedoids.computeClusterSet(numericData, euclidean, params);
        ClusterSet secondRun = kmedoids.computeClusterSet(numericData, euclidean, params);

        assertEquals(firstRun.toString(), secondRun.toString(), "Runs with same seed should produce identical results");
    }
}
