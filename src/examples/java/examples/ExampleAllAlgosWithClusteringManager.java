package examples;

import java.util.HashMap;

import service.ClusteringManager;
import dom.DataCollection;
import dom.NumericObject;
import dom.StringObject;

/**
 * Extended driver to run multiple clustering algorithms on string and numeric data.
 */
public class ExampleAllAlgosWithClusteringManager {

    public static void main(String[] args) {
        // === STRING DATA ===
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "abc", "schema1"));
        stringData.add(new StringObject(2, "abd", "schema2"));
        stringData.add(new StringObject(3, "xyz", "schema3"));

        // k-Med on strings
        HashMap<String, String> kMedDtwParams = new HashMap<>();
        kMedDtwParams.put("k", "2");
        kMedDtwParams.put("randomSeed", "42");
        kMedDtwParams.put("numRuns", "3");

        ClusteringManager kMedDtwManager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.KMEDOIDS)
                .withDistance(ClusteringManager.DistanceFunctionType.DTW)
                .withParams(kMedDtwParams)
                .build();

        System.out.println("String KMedoids with DTW result:");
        System.out.println(kMedDtwManager.execute());
        
        // HAC on strings
        HashMap<String, String> hacParams = new HashMap<>();
        hacParams.put("targetClusters", "2");

        ClusteringManager hacManager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(hacParams)
                .build();

        System.out.println("=== HAC on strings ===");
        System.out.println(hacManager.execute());

        // DBSCAN on strings
        HashMap<String, String> dbscanParams = new HashMap<>();
        dbscanParams.put("eps", "2");
        dbscanParams.put("minPts", "1");

        ClusteringManager dbscanManager = new ClusteringManager.Builder()
                .withData(stringData)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(dbscanParams)
                .build();

        System.out.println("=== DBSCAN on strings ===");
        System.out.println(dbscanManager.execute());


        // === NUMERIC DATA ===
        DataCollection numericData = new DataCollection();
        numericData.add(new NumericObject(1, new Double[]{1.0, 2.0}, "vector1"));
        numericData.add(new NumericObject(2, new Double[]{2.0, 2.5}, "vector2"));
        numericData.add(new NumericObject(3, new Double[]{10.0, 10.0}, "vector3"));

        // HAC on numeric
        ClusteringManager hacNumericManager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(hacParams)
                .build();

        System.out.println("=== HAC on numeric ===");
        System.out.println(hacNumericManager.execute());

        // DBSCAN on numeric
        dbscanParams.put("eps", "5");
        dbscanParams.put("minPts", "1");

        ClusteringManager dbscanNumericManager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(dbscanParams)
                .build();

        System.out.println("=== DBSCAN on numeric ===");
        System.out.println(dbscanNumericManager.execute());
    
        //kMeans on numeric
        HashMap<String, String> kMeansParams = new HashMap<>();
        kMeansParams.put("k", "2");

        ClusteringManager kMeansManager = new ClusteringManager.Builder()
                .withData(numericData)
                .withAlgorithm(ClusteringManager.AlgorithmType.KMEANS)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(kMeansParams)
                .build();

        System.out.println("Numeric KMeans with EuclideanDistance result:");
        System.out.println(kMeansManager.execute());
    }
}
