package distanceFunctions;

import dom.IClusterableObject;

/**
 * Generic interface for computing distances between clusterable objects.
 * <p>
 * Implementations can support strings (Levenshtein, DTW), numeric vectors (Euclidean, cosine),
 * or any other object type where a meaningful distance metric exists.
 * <p>
 * All distances should be non-negative and symmetric where possible (d(a,b) = d(b,a)).
 */
public interface IDistanceFunction {

    /**
     * Compute the distance between two clusterable objects.
     *
     * @param object1 The first object.
     * @param object2 The second object.
     * @return A non-negative double representing the distance between object1 and object2.
     * @throws IllegalArgumentException If either object is null or incompatible with the distance function.
     */
    Double computeDistance(IClusterableObject object1, IClusterableObject object2);
}
