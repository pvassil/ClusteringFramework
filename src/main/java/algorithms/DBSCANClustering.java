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
 * DBSCAN clustering implementation (general-purpose).
 * Uses an arbitrary distance function (strings, numeric, etc.) and a DistanceMatrix.
 */
public class DBSCANClustering implements IClusteringAlgorithm {

	/**
	 * Performs DBSCAN clustering on a collection of {@link IClusterableObject}.
	 * <p>
	 * DBSCAN (Density-Based Spatial Clustering of Applications with Noise) identifies clusters based on density:
	 * core points must have at least minPts neighbors within a distance of eps. Points not reachable from any
	 * core point are marked as noise (not included in any cluster).
	 * <p>
	 * This implementation is general-purpose: it relies on a provided {@link IDistanceFunction} for
	 * pairwise distances, so it works for string, numeric, or other custom data types.
	 *
	 * @param data The collection of {@link IClusterableObject} to be clustered.
	 * @param distanceFunction The {@link IDistanceFunction} used to compute distances between objects.
	 * @param parameters Optional parameters for the algorithm:
	 *                   <ul>
	 *                     <li>"eps" - double specifying the neighborhood radius (default = 1.0)</li>
	 *                     <li>"minPts" - integer specifying the minimum points to form a core (default = 2)</li>
	 *                   </ul>
	 * @return A {@link ClusterSet} containing all clusters found. Noise points are not included in any cluster.
	 * @throws NumberFormatException If "eps" or "minPts" parameters are provided but cannot be parsed to a double or integer. 
	 * @throws IllegalArgumentException If distanceFunction returns null or data is empty.
	 */

	@Override
    public ClusterSet computeClusterSet(DataCollection data,
                                        IDistanceFunction distanceFunction,
                                        HashMap<String, String> parameters) {
        int n = data.size();
        ClusterSet clusterSet = new ClusterSet(data, distanceFunction);
        if (n == 0) return clusterSet;

        // DBSCAN parameters
        double eps = 1.0; // default neighborhood radius
        int minPts = 2;   // default minimum points to form a core
        if (parameters != null) {
        	try {
        	    if (parameters.containsKey("eps")) eps = Double.parseDouble(parameters.get("eps"));
        	    if (parameters.containsKey("minPts")) minPts = Integer.parseInt(parameters.get("minPts"));
        	} catch (NumberFormatException e) {
        	    throw new IllegalArgumentException("Invalid DBSCAN parameter: eps or minPts must be numeric", e);
        	}

        }

        // Prepare distance matrix
        DistanceMatrix distMatrix = new DistanceMatrix(data, distanceFunction);
        distMatrix.compute();

        // Track visited points and cluster assignments
        boolean[] visited = new boolean[n];
        int[] clusterIds = new int[n]; // 0 = noise, 1..k = cluster
        int clusterCounter = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            visited[i] = true;
            List<Integer> neighbors = regionQuery(i, data, distMatrix, eps);
            if (neighbors.size() < minPts) {
                clusterIds[i] = 0; // noise
            } else {
                clusterCounter++;
                expandCluster(i, neighbors, clusterCounter, clusterIds, visited, data, distMatrix, eps, minPts);
            }
        }

        // Build ClusterSet from clusterIds
        for (int c = 1; c <= clusterCounter; c++) {
            Cluster cluster = new Cluster(c);
            for (int i = 0; i < n; i++) {
                if (clusterIds[i] == c) {
                    cluster.addMember(data.get(i));
                }
            }
            clusterSet.addCluster(cluster);
        }

        return clusterSet;
    }

    private void expandCluster(int pointIdx, List<Integer> neighbors, int clusterId,
                               int[] clusterIds, boolean[] visited,
                               DataCollection data, DistanceMatrix distMatrix,
                               double eps, int minPts) {

        clusterIds[pointIdx] = clusterId;

        List<Integer> seeds = new ArrayList<>(neighbors);
        int index = 0;
        while (index < seeds.size()) {
            int curr = seeds.get(index);
            if (!visited[curr]) {
                visited[curr] = true;
                List<Integer> currNeighbors = regionQuery(curr, data, distMatrix, eps);
                if (currNeighbors.size() >= minPts) {
                    for (int nIdx : currNeighbors) {
                        if (!seeds.contains(nIdx)) seeds.add(nIdx);
                    }
                }
            }
            if (clusterIds[curr] == 0) {
                clusterIds[curr] = clusterId;
            }
            index++;
        }
    }

    private List<Integer> regionQuery(int pointIdx, DataCollection data,
                                      DistanceMatrix distMatrix, double eps) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (distMatrix.getDistance(data.get(pointIdx), data.get(i)) <= eps) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }
}
