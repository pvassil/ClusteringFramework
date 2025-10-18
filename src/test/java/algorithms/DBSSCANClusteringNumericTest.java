package algorithms;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dom.DataCollection;
import dom.IClusterableObject;
import result.Cluster;
import result.ClusterSet;
import service.ClusteringManager;
import service.loaders.DataNumericLoader;

public class DBSSCANClusteringNumericTest {
     private DataCollection fifaData;
    private final String FIFA_FILE = "src/test/resources/input/CLEAN_FIFA17_official_data_SMALL.csv";
    
    @Before
    public void setUp() throws IOException {
        DataNumericLoader loader = new DataNumericLoader();
        fifaData = loader.load(FIFA_FILE, ",");
        
        // Verify data loaded correctly (60 players including header count)
        assertEquals("Should load 60 FIFA players", 60, fifaData.getCollectionData().size());
    }
    
    @Test
    public void testDBSCANWithExpectedResults() throws Exception {
        // Setup DBSCAN clustering with eps=2 and minPts=1
        HashMap<String, String> dbscanParams = new HashMap<>();
        dbscanParams.put("eps", "2");
        dbscanParams.put("minPts", "1");
        
        ClusteringManager dbscanManager = new ClusteringManager.Builder()
                .withData(fifaData)
                .withAlgorithm(ClusteringManager.AlgorithmType.DBSCAN)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(dbscanParams)
                .build();
        
        ClusterSet result = dbscanManager.execute();
        
        // Print actual results for comparison
        System.out.println("DBSCAN Clustering Results (eps=2, minPts=1):");
        printDetailedClusterResults(result);
        
        // Verify we get exactly 54 clusters based on your expected results
        assertEquals("Should produce exactly 54 clusters", 54, result.getClusters().size());
        
        // Verify all data points are assigned
        int totalAssigned = result.getClusters().stream()
                .mapToInt(cluster -> cluster.getMembers().size())
                .sum();
        assertEquals("All data points should be assigned to clusters", 
                fifaData.getCollectionData().size(), totalAssigned);
        
        // Verify specific cluster formations
        verifySpecificClusterFormations(result);
        
        // Verify cluster size distribution
        verifyClusterSizeDistribution(result);
    }
    
    private void verifySpecificClusterFormations(ClusterSet result) {
        // Verify that A. Traoré, Filipe Ferreira, and C. Mavinga are together (Cluster 5)
        boolean foundThreePlayerCluster = result.getClusters().stream()
            .anyMatch(cluster -> {
                if (cluster.getMembers().size() == 3) {
                    Set<String> names = new HashSet<>();
                    cluster.getMembers().forEach(obj -> names.add(obj.getMetaInfo()));
                    return names.contains("A. Traoré") && 
                           names.contains("Filipe Ferreira") && 
                           names.contains("C. Mavinga");
                }
                return false;
            });
        
        assertTrue("A. Traoré, Filipe Ferreira, and C. Mavinga should be in the same 3-member cluster", 
                foundThreePlayerCluster);
        
        // Verify that R. Würtz and Fernando Ferreira are together (Cluster 7)
        boolean foundTwoPlayerCluster1 = result.getClusters().stream()
            .anyMatch(cluster -> {
                if (cluster.getMembers().size() == 2) {
                    Set<String> names = new HashSet<>();
                    cluster.getMembers().forEach(obj -> names.add(obj.getMetaInfo()));
                    return names.contains("R. Würtz") && names.contains("Fernando Ferreira");
                }
                return false;
            });
        
        assertTrue("R. Würtz and Fernando Ferreira should be in the same 2-member cluster", 
                foundTwoPlayerCluster1);
        
        // Verify that N. Acevedo and A. Costa are together (Cluster 9)
        boolean foundTwoPlayerCluster2 = result.getClusters().stream()
            .anyMatch(cluster -> {
                if (cluster.getMembers().size() == 2) {
                    Set<String> names = new HashSet<>();
                    cluster.getMembers().forEach(obj -> names.add(obj.getMetaInfo()));
                    return names.contains("N. Acevedo") && names.contains("A. Costa");
                }
                return false;
            });
        
        assertTrue("N. Acevedo and A. Costa should be in the same 2-member cluster", 
                foundTwoPlayerCluster2);
        
        // Verify that M. Urbańczyk and M. Ødegaard are together (Cluster 26)
        boolean foundTwoPlayerCluster3 = result.getClusters().stream()
            .anyMatch(cluster -> {
                if (cluster.getMembers().size() == 2) {
                    Set<String> names = new HashSet<>();
                    cluster.getMembers().forEach(obj -> names.add(obj.getMetaInfo()));
                    return names.contains("M. Urbańczyk") && names.contains("M. Ødegaard");
                }
                return false;
            });
        
        assertTrue("M. Urbańczyk and M. Ødegaard should be in the same 2-member cluster", 
                foundTwoPlayerCluster3);
        
        // Verify that Y. Leiva and M. Sarr are together (Cluster 34)
        boolean foundTwoPlayerCluster4 = result.getClusters().stream()
            .anyMatch(cluster -> {
                if (cluster.getMembers().size() == 2) {
                    Set<String> names = new HashSet<>();
                    cluster.getMembers().forEach(obj -> names.add(obj.getMetaInfo()));
                    return names.contains("Y. Leiva") && names.contains("M. Sarr");
                }
                return false;
            });
        
        assertTrue("Y. Leiva and M. Sarr should be in the same 2-member cluster", 
                foundTwoPlayerCluster4);
        
        // Verify high-value singleton clusters
        String[] singletonPlayers = {"M. Arnold", "R. Gagliardini", "W. Janssen", "M. Rohdén", 
                                   "A. Surman", "Wilson Eduardo", "W. Szczęsny"};
        
        for (String playerName : singletonPlayers) {
            boolean foundInSingletonCluster = result.getClusters().stream()
                .anyMatch(cluster -> 
                    cluster.getMembers().size() == 1 && 
                    cluster.getMembers().get(0).getMetaInfo().equals(playerName));
            
            assertTrue(playerName + " should be in a singleton cluster", foundInSingletonCluster);
        }
    }
    
    private void verifyClusterSizeDistribution(ClusterSet result) {
        // Count clusters by size
        long singletonClusters = result.getClusters().stream()
            .mapToInt(cluster -> cluster.getMembers().size())
            .filter(size -> size == 1)
            .count();
        
        long twoMemberClusters = result.getClusters().stream()
            .mapToInt(cluster -> cluster.getMembers().size())
            .filter(size -> size == 2)
            .count();
        
        long threeMemberClusters = result.getClusters().stream()
            .mapToInt(cluster -> cluster.getMembers().size())
            .filter(size -> size == 3)
            .count();
        
        // Based on your expected results: 49 singleton, 4 two-member, 1 three-member clusters
        assertEquals("Should have 49 singleton clusters", 49, singletonClusters);
        assertEquals("Should have 4 two-member clusters", 4, twoMemberClusters);
        assertEquals("Should have 1 three-member cluster", 1, threeMemberClusters);
        
        System.out.println("\nCluster Size Distribution:");
        System.out.println("Singleton clusters: " + singletonClusters);
        System.out.println("Two-member clusters: " + twoMemberClusters);
        System.out.println("Three-member clusters: " + threeMemberClusters);
    }
    
    private void printDetailedClusterResults(ClusterSet result) {
        for (int i = 0; i < result.getClusters().size(); i++) {
            Cluster cluster = result.getClusters().get(i);
            System.out.println("---------- Cluster " + i + " (Size: " + 
                    cluster.getMembers().size() + ") ----------");
            
            for (IClusterableObject obj : cluster.getMembers()) {
                System.out.printf("%-20s | Overall: %s | Potential: %s | Value: %s%n",
                    obj.getMetaInfo(),
                    obj.getNumericalValues()[0],
                    obj.getNumericalValues()[1], 
                    obj.getNumericalValues()[2]);
            }
            System.out.println("--------------------------");
        }
        System.out.println();
    }
}
