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

public class HACClusteringNumericTest {

    private DataCollection fifaData;
    private final String FIFA_FILE = "src/test/resources/input/CLEAN_FIFA17_official_data_SMALL.csv";
    
    @Before
    public void setUp() throws IOException {
        DataNumericLoader loader = new DataNumericLoader();
        fifaData = loader.load(FIFA_FILE, ",");
        
        // Verify data loaded correctly
        assertEquals("Should load 60 FIFA players", 60, fifaData.getCollectionData().size());
    }
    
    @Test
    public void testHACWith7ClustersExpectedResults() throws Exception {
        // Setup HAC clustering with 7 clusters
        HashMap<String, String> hacParams = new HashMap<>();
        hacParams.put("targetClusters", "7");
        
        ClusteringManager hacManager = new ClusteringManager.Builder()
                .withData(fifaData)
                .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
                .withDistance(ClusteringManager.DistanceFunctionType.EUCLIDEAN)
                .withParams(hacParams)
                .build();
        
        ClusterSet result = hacManager.execute();
        
        // Verify we get exactly 7 clusters
        assertEquals("Should produce exactly 7 clusters", 7, result.getClusters().size());
        
        // Print actual results for comparison
        System.out.println("HAC Clustering Results:");
        printDetailedClusterResults(result);
        
        // Verify all data points are assigned
        int totalAssigned = result.getClusters().stream()
                .mapToInt(cluster -> cluster.getMembers().size())
                .sum();
        assertEquals("All data points should be assigned to clusters", 
                fifaData.getCollectionData().size(), totalAssigned);
        
        // Test that we have the expected cluster size distribution
        int[] actualClusterSizes = result.getClusters().stream()
                .mapToInt(cluster -> cluster.getMembers().size())
                .sorted()
                .toArray();

        System.err.println();
        for(int i=0;i<actualClusterSizes.length;i++)
        	System.err.println(actualClusterSizes[i] + "\t");
        
        int[] expectedClusterSizes = {1, 1, 1, 1, 2, 3, 51}; //ORANGE
        assertArrayEquals("Cluster sizes should match expected distribution", 
                expectedClusterSizes, actualClusterSizes);
        
        // Verify specific high-value players are in singleton clusters
        verifyHighValuePlayersInSeparateClusters(result);
    }
    
    private void verifyHighValuePlayersInSeparateClusters(ClusterSet result) {
        // Based on your expected results, these players should be in singleton clusters
        String[] singletonPlayers = {"W. Szczęsny", "M. Arnold", "R. Gagliardini"};
        
        for (String playerName : singletonPlayers) {
            boolean foundInSingletonCluster = result.getClusters().stream()
                .anyMatch(cluster -> 
                    cluster.getMembers().size() == 1 && 
                    cluster.getMembers().get(0).getMetaInfo().equals(playerName));
            
            assertTrue(playerName + " should be in a singleton cluster", foundInSingletonCluster);
        }
        
        // Verify Wilson Eduardo and S. Terodde are together in a 2-member cluster
        boolean foundTwoPlayerCluster = result.getClusters().stream()
            .anyMatch(cluster -> {
                if (cluster.getMembers().size() == 2) {
                    Set<String> names = new HashSet<>();
                    cluster.getMembers().forEach(obj -> names.add(obj.getMetaInfo()));
                    return names.contains("Wilson Eduardo") && names.contains("S. Terodde");
                }
                return false;
            });
        
        assertTrue("Wilson Eduardo and S. Terodde should be in the same 2-member cluster", 
                foundTwoPlayerCluster);
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