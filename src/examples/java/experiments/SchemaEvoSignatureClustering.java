package experiments;


import java.io.IOException;
import java.util.HashMap;

import service.ClusteringManager;
import dom.DataCollection;
import dom.IClusterableObject;
import service.loaders.DataStringLoader;
import result.ClusterSet;

public class SchemaEvoSignatureClustering {

    public static void main(String[] args) throws Exception {
        String stringFile = "src/test/resources/input/schemaEvo/Level11SgnNoGestation.tsv"; //input file
        //String outputFolder = "src/test/resources/output/";  //output **Folder**

        System.out.println("=== Loading (String) Data on Schema Evo Histories ===");
        DataCollection schemaSignaturesLoaded = null;
        DataStringLoader loader = new DataStringLoader();
		try {
			schemaSignaturesLoaded = loader.load(stringFile, "\t");
		} catch (IOException e) {
			System.err.println("Failed to open input file"
					+ "\n\t" + stringFile +"\n" + e.toString());
			System.exit(-1);
		}
		
        for (IClusterableObject obj : schemaSignaturesLoaded.getCollectionData()) {
            System.out.println(obj.getId() + " | " + obj.getMetaInfo() + " | " + obj.getStringValue());
        }
        System.out.println("======\n\n");

        String NUMBER_OF_CLUSTERS_STR = "7";
        
        // HAC on strings
        HashMap<String, String> hacParams = new HashMap<>();
        hacParams.put("targetClusters", NUMBER_OF_CLUSTERS_STR);

        ClusteringManager hacManager = new ClusteringManager.Builder()
                .withData(schemaSignaturesLoaded)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(hacParams)
//                .withOutputFile(outputFolder + "HACEdit.txt")
                .build();

        System.out.println("=== HAC + EDIT on strings ===");
        System.out.println(hacManager.execute());
        System.out.println("======\n\n");
        
        hacManager = new ClusteringManager.Builder()
                .withData(schemaSignaturesLoaded)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.DTW)
                .withParams(hacParams)
//                .withOutputFile(outputFolder + "HACDTW.txt")
                .build();

        System.out.println("=== HAC + DTW on strings ===");
        System.out.println(hacManager.execute());
        System.out.println("======\n\n");
        
        // DBSCAN on strings
        HashMap<String, String> dbscanParams = new HashMap<>();
        dbscanParams.put("eps", "2");
        dbscanParams.put("minPts", "1");

        ClusteringManager dbscanManager = new ClusteringManager.Builder()
                .withData(schemaSignaturesLoaded)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
                .withParams(dbscanParams)
  //              .withOutputFile(outputFolder + "DBSCANEdit.txt")                
                .build();

        System.out.println("=== DBSCAN + EDIT on strings ===");
        System.out.println(dbscanManager.execute());
        System.out.println("======\n\n");

        
        dbscanManager = new ClusteringManager.Builder()
                .withData(schemaSignaturesLoaded)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.DTW)
                .withParams(dbscanParams)
//                .withOutputFile(outputFolder + "DBSCANDTW.txt")
                .build();

        System.out.println("=== DBSCAN + DTW on strings ===");
        System.out.println(dbscanManager.execute());
        System.out.println("======\n\n");
        

        // //////////////////////////////////////////
        // K MEDOIDS IS TOOOOOOOO SLOW !!!!!
        // //////////////////////////////////////////        
        HashMap<String, String> stringParams = new HashMap<>();
        stringParams.put("k", NUMBER_OF_CLUSTERS_STR);
        stringParams.put("randomSeed", "42");
        stringParams.put("numRuns", "3");

        // //////////////////////////////////////////
        // K-MED+DTW TOOOOOOOO SLOW !!!!!
        // //////////////////////////////////////////                 
        ClusteringManager kMedoidsManagerDTW = new ClusteringManager.Builder()
                .withData(schemaSignaturesLoaded)
                .withAlgorithm(ClusteringManager.AlgorithmType.KMEDOIDS)
                .withDistance(ClusteringManager.DistanceFunctionType.DTW)
                .withParams(stringParams)
//                .withOutputFile(outputFolder + "K-MedDTW.txt")
                .build();

        ClusterSet kMedoidClustersDTW = kMedoidsManagerDTW.execute();
        System.out.println("String KMedoids with DTW result:");
        System.out.println(kMedoidClustersDTW);
        System.out.println("======\n\n");
        

        // //////////////////////////////////////////
        // MOTHER OF GOD, WAY TOOOOOOOO SLOW !!!!!
        // //////////////////////////////////////////        
        stringParams.put("verbose", "true");
        ClusteringManager kMedoidsManager = new ClusteringManager.Builder()
              .withData(schemaSignaturesLoaded)
              .withAlgorithm(ClusteringManager.AlgorithmType.KMEDOIDS)
              .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
              .withParams(stringParams)
//              .withOutputFile(outputFolder + "K-MedEdit.txt")            
              .build();

      ClusterSet kMedoidClusters = kMedoidsManager.execute();
      System.out.println("String KMedoids with EDIT result:");
      System.out.println(kMedoidClusters);
      System.out.println("======\n\n");

        
    }//end main
}//end class
