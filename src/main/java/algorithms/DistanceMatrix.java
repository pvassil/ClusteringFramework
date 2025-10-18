package algorithms;

import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.IClusterableObject;

//import java.util.HashMap;
//import java.util.Map;

/**
 * A utility class to store pairwise distances between IClusterableObjects.
 * Can be used to avoid recomputing distances in matrix-based clustering algorithms.
 */
public class DistanceMatrix {

    private double[][] matrix;
    private DataCollection data;
    private IDistanceFunction distanceFunction;
    private boolean computed;

    /**
     * Constructor.
     *
     * @param data Collection of objects.
     * @param distanceFunction Distance function used to compute pairwise distances.
     */
    public DistanceMatrix(DataCollection data, IDistanceFunction distanceFunction) {
        this.data = data;
        this.distanceFunction = distanceFunction;
        int n = data.size();
        this.matrix = new double[n][n];
        this.computed = false;
    }

    /**
     * Compute the full distance matrix. This will fill the internal double[][] array.
     */
    public void compute() {
        int n = data.size();
        for (int i = 0; i < n; i++) {
            matrix[i][i] = 0.0;
            for (int j = i + 1; j < n; j++) {
                double d = distanceFunction.computeDistance(data.get(i), data.get(j));
                matrix[i][j] = d;
                matrix[j][i] = d;
            }
        }
        computed = true;
    }

    /**
     * Get the distance between two objects.
     * @param o1 First object
     * @param o2 Second object
     * @return Distance as double
     */
    public double getDistance(IClusterableObject o1, IClusterableObject o2) {
        if (!computed) {
            compute();
        }
        int i = o1.getId() - 1; // assumes IDs start at 1 and correspond to data index
        int j = o2.getId() - 1;
        return matrix[i][j];
    }

    /**
     * Optional: get internal matrix (read-only copy)
     */
    public double[][] getMatrix() {
        if (!computed) compute();
        int n = matrix.length;
        double[][] copy = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    /**
     * Number of objects in the matrix
     */
    public int size() {
        return data.size();
    }

    /**
     * For debugging: print the matrix
     */
    public void printMatrix() {
        if (!computed) compute();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%8.2f ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
