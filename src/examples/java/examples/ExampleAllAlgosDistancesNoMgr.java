package examples;

import java.util.HashMap;

import algorithms.DBSCANClustering;
import algorithms.HierarchicalAgglomerativeClustering;
import algorithms.IClusteringAlgorithm;
import algorithms.KMeansClustering;
import algorithms.KMedoidsClustering;
import distanceFunctions.DTWDistance;
import distanceFunctions.EditDistance;
import distanceFunctions.EuclideanDistance;
import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.NumericObject;
import dom.StringObject;
import result.ClusterSet;

public class ExampleAllAlgosDistancesNoMgr {

    public static void main(String[] args) {

        // === STRING DATA ===
        System.out.println("=== STRING DATA with EditDistance ===");
        DataCollection stringData = new DataCollection();
        stringData.add(new StringObject(1, "-(4)b(9)_(13)c(18)", "schema1"));
        stringData.add(new StringObject(2, "-(4)b(9)_(14)c(20)", "schema2"));
        stringData.add(new StringObject(3, "-(3)b(8)_(12)c(4)c(15)c(7)_(56)", "schema3"));

        IDistanceFunction editDist = new EditDistance();
        IDistanceFunction dtwDist = new DTWDistance(); // DTW for sequences

        HashMap<String, String> paramsHAC = new HashMap<>();
        paramsHAC.put("targetClusters", "2");

        // --- HAC ---
        IClusteringAlgorithm hac = new HierarchicalAgglomerativeClustering();
        ClusterSet hacEdit = hac.computeClusterSet(stringData, editDist, paramsHAC);
        System.out.println("HAC Result (EditDistance): " + hacEdit);

        ClusterSet hacDTW = hac.computeClusterSet(stringData, dtwDist, paramsHAC);
        System.out.println("HAC Result (DTWDistance): " + hacDTW);

        // --- DBSCAN ---
        IClusteringAlgorithm dbscan = new DBSCANClustering();
        HashMap<String, String> dbscanParams = new HashMap<>();
        dbscanParams.put("eps", "10");
        dbscanParams.put("minPts", "1");

        ClusterSet dbscanEdit = dbscan.computeClusterSet(stringData, editDist, dbscanParams);
        System.out.println("DBSCAN Result (EditDistance): " + dbscanEdit);

        ClusterSet dbscanDTW = dbscan.computeClusterSet(stringData, dtwDist, dbscanParams);
        System.out.println("DBSCAN Result (DTWDistance): " + dbscanDTW);

        // --- KMedoids ---
        IClusteringAlgorithm kmedoids = new KMedoidsClustering();
        HashMap<String, String> kmedoidsParams = new HashMap<>();
        kmedoidsParams.put("k", "2");
        kmedoidsParams.put("randomSeed", "42");
        kmedoidsParams.put("maxIterations", "50");
        kmedoidsParams.put("numRuns", "5");
        kmedoidsParams.put("verbose", "false");

        ClusterSet kmedoidsEdit = kmedoids.computeClusterSet(stringData, editDist, kmedoidsParams);
        System.out.println("KMedoids Result (EditDistance): " + kmedoidsEdit);

        ClusterSet kmedoidsDTW = kmedoids.computeClusterSet(stringData, dtwDist, kmedoidsParams);
        System.out.println("KMedoids Result (DTWDistance): " + kmedoidsDTW);


        // === NUMERIC DATA ===
        System.out.println("\n=== NUMERIC DATA with EuclideanDistance ===");
        DataCollection numericData = new DataCollection();
        numericData.add(new NumericObject(1, new Double[]{1.0, 2.0}, "vector1"));
        numericData.add(new NumericObject(2, new Double[]{2.0, 2.5}, "vector2"));
        numericData.add(new NumericObject(3, new Double[]{10.0, 10.0}, "vector3"));

        IDistanceFunction euclidean = new EuclideanDistance();

        // HAC numeric
        ClusterSet hacNumeric = hac.computeClusterSet(numericData, euclidean, paramsHAC);
        System.out.println("HAC Result (numeric): " + hacNumeric);

        // DBSCAN numeric
        dbscanParams.put("eps", "5");
        dbscanParams.put("minPts", "1");
        ClusterSet dbscanNumeric = dbscan.computeClusterSet(numericData, euclidean, dbscanParams);
        System.out.println("DBSCAN Result (numeric): " + dbscanNumeric);

        // KMeans numeric
        IClusteringAlgorithm kmeans = new KMeansClustering();
        HashMap<String, String> kmeansParams = new HashMap<>();
        kmeansParams.put("k", "2");
        ClusterSet kmeansNumeric = kmeans.computeClusterSet(numericData, euclidean, kmeansParams);
        System.out.println("KMeans Result (numeric): " + kmeansNumeric);

        // KMedoids numeric
        ClusterSet kmedoidsNumeric = kmedoids.computeClusterSet(numericData, euclidean, kmedoidsParams);
        System.out.println("KMedoids Result (numeric): " + kmedoidsNumeric);
    }
}
