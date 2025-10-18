package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.IClusterableObject;
import result.Cluster;
import result.ClusterSet;

/**
 * Hierarchical Agglomerative Clustering (single linkage) using a precomputed DistanceMatrix.
 */
public class HierarchicalAgglomerativeClustering implements IClusteringAlgorithm {

	/**
	 * Performs hierarchical agglomerative clustering on a given collection of objects.
	 * <p>
	 * The clustering algorithm is data-agnostic: it uses the provided {@link IDistanceFunction} to compute
	 * distances between any two objects, so it can handle strings, numeric vectors, or any other object type.
	 * Single linkage (minimum distance between any members of two clusters) is used in this implementation.
	 * <p>
	 * The algorithm starts with each object in its own cluster and iteratively merges the closest pair
	 * of clusters until the target number of clusters is reached.
	 *
	 * @param data The collection of {@link IClusterableObject} to be clustered.
	 * @param distanceFunction The {@link IDistanceFunction} used to compute pairwise distances between objects.
	 * @param parameters Optional parameters for the algorithm. Supported keys:
	 *                   <ul>
	 *                     <li>"targetClusters" - integer specifying the desired number of clusters (default = 1)</li>
	 *                   </ul>
	 * @return A {@link ClusterSet} containing the resulting clusters after the agglomerative process.
	 * @throws IllegalArgumentException If distanceFunction returns null or data is empty.
	 */
	@Override
    public ClusterSet computeClusterSet(DataCollection data,
                                        IDistanceFunction distanceFunction,
                                        HashMap<String, String> parameters) {
        int n = data.size();
        if (n == 0) return new ClusterSet(data, distanceFunction);

        // Initialize clusters: each object in its own cluster
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Cluster c = new Cluster(i + 1);
            c.addMember(data.get(i));
            clusters.add(c);
        }

        // Compute distance matrix
        DistanceMatrix distMatrix = new DistanceMatrix(data, distanceFunction);
        distMatrix.compute();

        // Retrieve target number of clusters
        int targetClusters = 1;
        if (parameters != null && parameters.containsKey("targetClusters")) {
            targetClusters = Integer.parseInt(parameters.get("targetClusters"));
            if (targetClusters < 1) targetClusters = 1;
        }

        // Agglomerative clustering (single linkage)
        while (clusters.size() > targetClusters) {
            double minDist = Double.MAX_VALUE;
            int idxA = -1, idxB = -1;

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double d = computeLinkageDistance(clusters.get(i), clusters.get(j), distMatrix);
                    if (d < minDist) {
                        minDist = d;
                        idxA = i;
                        idxB = j;
                    }
                }
            }

            // Merge clusterB into clusterA
            Cluster clusterA = clusters.get(idxA);
            Cluster clusterB = clusters.get(idxB);
            for (IClusterableObject obj : clusterB.getMembers()) {
                clusterA.addMember(obj);
            }
            clusters.remove(idxB);
        }

        // Build and return ClusterSet
        ClusterSet clusterSet = new ClusterSet(data, distanceFunction);
        for (Cluster c : clusters) {
            clusterSet.addCluster(c);
        }
        return clusterSet;
    }

    // Single linkage: minimum distance between any member of two clusters
    private double computeLinkageDistance(Cluster c1, Cluster c2, DistanceMatrix distMatrix) {
        double minDist = Double.MAX_VALUE;
        for (IClusterableObject o1 : c1.getMembers()) {
            for (IClusterableObject o2 : c2.getMembers()) {
                double d = distMatrix.getDistance(o1, o2);
                if (d < minDist) minDist = d;
            }
        }
        return minDist;
    }
}
