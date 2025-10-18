package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.IClusterableObject;
import dom.NumericObject;
import result.Cluster;
import result.ClusterSet;

/**
 * K-Means clustering for numeric data (IClusterableObject returning Double[] via getNumericalValues()).
 */
public class KMeansClustering implements IClusteringAlgorithm {

	/**
	 * Performs K-Means clustering on numeric {@link IClusterableObject} data.
	 * <p>
	 * Each object must implement getNumericalValues(). Clusters are represented by the mean of assigned points.
	 *
	 * @param data Collection of numeric IClusterableObjects.
	 * @param distanceFunction Distance function (e.g., EuclideanDistance) between numeric vectors.
	 * @param parameters Optional:
	 *                   <ul>
	 *                     <li>"k" - number of clusters (default = 2)</li>
	 *                     <li>"maxIter" - maximum iterations (default = 100)</li>
	 *                   </ul>
	 * @return ClusterSet with k clusters.
	 */
	@Override
    public ClusterSet computeClusterSet(DataCollection data,
                                        IDistanceFunction distanceFunction,
                                        HashMap<String, String> parameters) {
        int n = data.size();
        if (n == 0) return new ClusterSet(data, distanceFunction);

        int k = 2;
        int maxIter = 100;
        if (parameters != null) {
            if (parameters.containsKey("k")) k = Integer.parseInt(parameters.get("k"));
            if (parameters.containsKey("maxIter")) maxIter = Integer.parseInt(parameters.get("maxIter"));
        }

        if (k > n) k = n;

        // Initialize centroids randomly
        List<NumericObject> centroids = new ArrayList<>();
        List<IClusterableObject> copy = new ArrayList<>(data.getCollectionData());
        Collections.shuffle(copy, new Random(42));
        for (int i = 0; i < k; i++) {
            Double[] values = copy.get(i).getNumericalValues();
            centroids.add(new NumericObject(-1, values, "centroid-" + (i + 1)));
        }

        int[] assignments = new int[n];
        boolean changed = true;
        int iter = 0;

        while (changed && iter < maxIter) {
            changed = false;
            iter++;

            // Assign points to nearest centroid
            for (int i = 0; i < n; i++) {
                double minDist = Double.MAX_VALUE;
                int bestCluster = 0;
                for (int j = 0; j < k; j++) {
                    double d = distanceFunction.computeDistance(
                            new NumericObject(data.get(i).getId(), data.get(i).getNumericalValues(), data.get(i).getMetaInfo()),
                            centroids.get(j));
                    if (d < minDist) {
                        minDist = d;
                        bestCluster = j;
                    }
                }
                if (assignments[i] != bestCluster) {
                    assignments[i] = bestCluster;
                    changed = true;
                }
            }

            // Update centroids
            for (int j = 0; j < k; j++) {
                int count = 0;
                Double[] sum = null;
                for (int i = 0; i < n; i++) {
                    if (assignments[i] == j) {
                        Double[] vals = data.get(i).getNumericalValues();
                        if (sum == null) sum = new Double[vals.length];
                        for (int d = 0; d < vals.length; d++) sum[d] = (sum[d] == null ? 0.0 : sum[d]) + vals[d];
                        count++;
                    }
                }
                if (count > 0) {
                    for (int d = 0; d < sum.length; d++) sum[d] /= count;
                    centroids.set(j, new NumericObject(-1, sum, "centroid-" + (j + 1)));
                }
            }
        }

        // Build clusters
        ClusterSet clusterSet = new ClusterSet(data, distanceFunction);
        for (int j = 0; j < k; j++) {
            Cluster cluster = new Cluster(j + 1);
            for (int i = 0; i < n; i++) {
                if (assignments[i] == j) cluster.addMember(data.get(i));
            }
            clusterSet.addCluster(cluster);
        }
        return clusterSet;
    }
}
