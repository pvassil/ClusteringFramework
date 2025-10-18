package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import dom.DataCollection;
import result.ClusterSet;
import service.loaders.DataNumericLoader;

public class ClusteringNumericDataTest {
    
	@Test
    public void testHAC() throws IOException {
		DataNumericLoader numericLoader = new DataNumericLoader();
		DataCollection numericData = numericLoader.load("src/test/resources/input/numericDataFile.csv", ","); 
    
    	HashMap<String, String> params = new HashMap<>();
    	params.put("targetClusters", "2");
    	
    	ClusteringManager manager = new ClusteringManager.Builder()
    	        .withData(numericData)
    	        .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
    	        .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
    	        .withParams(params)
    	        .withOutputFile("src/test/resources/output/Numeric_HAC_output.txt")
    	        .build();
    	
    	// Run & export
    	ClusterSet clusters = manager.executeAndExport();
    	assertEquals(2,clusters.size());
    }
	
	
	@Test
    public void testDBSCAN() throws IOException {
		DataNumericLoader numericLoader = new DataNumericLoader();
		DataCollection numericData = numericLoader.load("src/test/resources/input/numericDataFile.csv", ","); 
    
        HashMap<String, String> params = new HashMap<>();
        params.put("eps", "2.0");
        params.put("minPts", "1");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(params)
                .withOutputFile("src/test/resources/output/Numeric_DBSCAN_output.txt")
                .build();

        ClusterSet clusters = manager.executeAndExport();
    	assertEquals(2,clusters.size());

    }
	
	@Test
    public void testKmeans() throws IOException {
		DataNumericLoader numericLoader = new DataNumericLoader();
		DataCollection numericData = numericLoader.load("src/test/resources/input/numericDataFile.csv", ","); 
    
        HashMap<String, String> params = new HashMap<>();
        params.put("k", "2");

        ClusteringManager manager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.KMEANS)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(params)
                .withOutputFile("src/test/resources/output/Numeric_Kmeans_output.txt")
                .build();

        ClusterSet clusters = manager.executeAndExport();
    	assertEquals(2,clusters.size());


    }
}
