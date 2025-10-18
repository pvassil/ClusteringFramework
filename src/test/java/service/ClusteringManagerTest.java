package service;

import org.junit.jupiter.api.Test;

import dom.DataCollection;
import dom.IClusterableObject;
import dom.NumericObject;
import dom.StringObject;
import result.Cluster;
import result.ClusterSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the refactored {@link ClusteringManager} class.
 */
public class ClusteringManagerTest {

    @Test
    public void testHACWithEditDistance() {
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "abc", "s1"));
        stringData.add(new StringObject(2, "abd", "s2"));
        stringData.add(new StringObject(3, "xyz", "s3"));

        HashMap<String, String> params = new HashMap<>();
        params.put("targetClusters", "2");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(params)
                .build();

        ClusterSet clusters = manager.execute();
        assertEquals(2, clusters.size(), "HAC with EditDistance should produce 2 clusters");
    }

    @Test
    public void testKMedoidsWithDTW() {
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "abc", "s1"));
        stringData.add(new StringObject(2, "abd", "s2"));
        stringData.add(new StringObject(3, "xyz", "s3"));

        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");
        params.put("randomSeed", "42");
        params.put("numRuns", "3");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.KMEDOIDS)
                .withDistance(ClusteringManager.DistanceFunctionType.DTW)
                .withParams(params)
                .build();

        ClusterSet clusters = manager.execute();
        assertEquals(2, clusters.size(), "KMedoids with DTWDistance should produce 2 clusters");
    }

    @Test
    public void testEuclideanWithNumericData() {
        DataCollection numericData = new DataCollection();
        numericData.add(new NumericObject(1, new Double[]{1.0, 2.0}, "v1"));
        numericData.add(new NumericObject(2, new Double[]{2.0, 2.5}, "v2"));
        numericData.add(new NumericObject(3, new Double[]{10.0, 10.0}, "v3"));

        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.KMEANS)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(params)
                .build();

        ClusterSet clusters = manager.execute();
        assertEquals(2, clusters.size(), "KMeans with EuclideanDistance should produce 2 clusters");
    }

    @Test
    public void testInvalidDistanceThrowsException() {
        DataCollection numericData = new DataCollection();
        numericData.add(new NumericObject(1, new Double[]{1.0}, "v1"));

        HashMap<String, String> params = new HashMap<>();

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            new ClusteringManager.Builder()
                    .withData(numericData)
                    .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                    .withDistance(ClusteringManager.DistanceFunctionType.DTW)
                    .withParams(params)
                    .build()
        );

        assertTrue(ex.getMessage().contains("EditDistance/DTWDistance requires string data."));
    }

    @Test
    public void testOutputFileWriting() {
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "abc", "s1"));
        stringData.add(new StringObject(2, "abd", "s2"));

        HashMap<String, String> params = new HashMap<>();
        params.put("targetClusters", "1");

        String tempFile = "test_clusters_output.txt";

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(params)
                .withOutputFile(tempFile)
                .build();

        ClusterSet clusters = manager.execute();
        assertNotNull(clusters);
    }
    
    @Test
    public void testExecuteAndExport() throws IOException {
        // Prepare sample string data
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "aaa", "s1"));
        stringData.add(new StringObject(2, "aab", "s2"));
        stringData.add(new StringObject(3, "xyz", "s3"));

        HashMap<String, String> params = new HashMap<>();
        params.put("targetClusters", "2");

        // Temporary output file
        //File tempFile = File.createTempFile("resources/output/clusteringManager_test_ExecExpotrt", ".txt");    
        //tempFile.deleteOnExit(); // auto cleanup
        File tempFile = new File("src/test/resources/output/clusteringManager_test_ExecExport.txt");
        
        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(params)
                .withOutputFile(tempFile.getAbsolutePath())
                .build();

        // Execute and export
        @SuppressWarnings("unused")
		ClusterSet results = manager.executeAndExport();

        // Check file exists
        assertTrue(tempFile.exists());
        assertTrue(tempFile.length() > 0);

        // Read the file and collect lines
        Set<String> fileLines = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line.trim());
            }
        }

        // Verify each object's meta info appears in the file with a cluster id
        @SuppressWarnings("unused")
		Set<String> expectedMetaInfos = new HashSet<>();
        for (IClusterableObject obj : stringData.getCollectionData()) {
            boolean found = fileLines.stream().anyMatch(l -> l.startsWith(obj.getMetaInfo() + "\t"));
            assertTrue(found, "Mapping for " + obj.getMetaInfo() + " not found in exported file.");
        }

        // Optionally: assert number of lines matches total objects
        assertEquals(stringData.getCollectionData().size(), fileLines.size());
    }
    
    private Set<Integer> extractIds(Cluster cluster) {
        Set<Integer> ids = new HashSet<>();
        for (IClusterableObject obj : cluster.getMembers()) {
            ids.add(obj.getId());
        }
        return ids;
    }

    @Test
    public void testHACWithEditDistanceClusterContents() {
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "aaa", "s1"));
        stringData.add(new StringObject(2, "aab", "s2"));
        stringData.add(new StringObject(3, "xyz", "s3"));

        HashMap<String, String> params = new HashMap<>();
        params.put("targetClusters", "2");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(params)
                .build();

        ClusterSet clusters = manager.execute();
        assertEquals(2, clusters.size());

        // collect cluster contents
        List<Cluster> clusterList = new ArrayList<>(clusters.getClusters());
        Set<Integer> c1 = extractIds(clusterList.get(0));
        Set<Integer> c2 = extractIds(clusterList.get(1));

        Set<Integer> c12 = new HashSet<Integer>(); c12.add(1); c12.add(2);
        Set<Integer> c3 = new HashSet<Integer>(); c3.add(3); 
        // We expect ids {1,2} to be grouped, and {3} separate
        assertTrue(c1.equals(c12) || c2.equals(c12));
        assertTrue(c1.equals(c3) || c2.equals(c3));
    }

    @Test
    public void testDBSCANWithEuclideanClusterContents() {
        DataCollection numericData = new DataCollection();
        numericData.add(new NumericObject(1, new Double[]{1.0, 2.0}, "v1"));
        numericData.add(new NumericObject(2, new Double[]{1.5, 2.5}, "v2"));
        numericData.add(new NumericObject(3, new Double[]{10.0, 10.0}, "v3"));

        HashMap<String, String> params = new HashMap<>();
        params.put("eps", "2.0");
        params.put("minPts", "1");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(params)
                .build();

        ClusterSet clusters = manager.execute();
        assertEquals(2, clusters.size());

        // Expect cluster {1,2} and cluster {3}
        List<Cluster> clusterList = new ArrayList<>(clusters.getClusters());
        Set<Integer> c1 = extractIds(clusterList.get(0));
        Set<Integer> c2 = extractIds(clusterList.get(1));

        Set<Integer> c12 = new HashSet<Integer>(); c12.add(1); c12.add(2);
        Set<Integer> c3 = new HashSet<Integer>(); c3.add(3); 
        // We expect ids {1,2} to be grouped, and {3} separate
        assertTrue(c1.equals(c12) || c2.equals(c12));
        assertTrue(c1.equals(c3) || c2.equals(c3));
    }
    
}//end class
