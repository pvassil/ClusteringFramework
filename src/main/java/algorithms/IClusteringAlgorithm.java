package algorithms;

import java.util.HashMap;

import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import result.ClusterSet;

public interface IClusteringAlgorithm {
    /**
     * Compute clusters for a given collection using a specified distance function.
     * @param data The collection of objects to cluster
     * @param distanceFunction The distance function to use (string, numeric, etc.)
     * @param parameters Optional parameters for the algorithm
     * @return ClusterSet containing the resulting clusters
     */
    ClusterSet computeClusterSet(DataCollection data, IDistanceFunction distanceFunction,
                                 HashMap<String, String> parameters);
}
