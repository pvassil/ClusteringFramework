package distanceFunctions;

import dom.IClusterableObject;

/**
 * Computes the Dynamic Time Warping (DTW) distance between two objects
 * with string representations of sequences.
 *
 * Each object’s sequence is represented by the {@link IClusterableObject#getStringValue()}.
 * The DTW distance measures similarity between sequences allowing for elastic
 * alignment (useful for time series or schema life sequences).
 */
public class DTWDistance implements IStringDistanceFunction {

    private double insertionCost;
    private double deletionCost;
    private double substitutionCost;

    /**
     * Default constructor with all costs = 1.0.
     */
    public DTWDistance() {
        this(1.0, 1.0, 1.0);
    }

    /**
     * Constructor with custom costs.
     *
     * @param insertionCost Cost of inserting an element
     * @param deletionCost  Cost of deleting an element
     * @param substitutionCost Cost of substituting an element
     */
    public DTWDistance(double insertionCost, double deletionCost, double substitutionCost) {
        this.insertionCost = insertionCost;
        this.deletionCost = deletionCost;
        this.substitutionCost = substitutionCost;
    }

    /**
     * Computes the DTW distance between two clusterable objects.
     *
     * @param obj1 First object
     * @param obj2 Second object
     * @return non-negative distance (0 if sequences are identical)
     */
    @Override
    public Double computeDistance(IClusterableObject obj1, IClusterableObject obj2) {
        String s1 = obj1.getStringValue();
        String s2 = obj2.getStringValue();

        int n = s1.length();
        int m = s2.length();

        double[][] dtw = new double[n + 1][m + 1];

        // Initialize DTW matrix
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dtw[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        dtw[0][0] = 0.0;

        // Compute DTW
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                double cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0.0 : substitutionCost;
                dtw[i][j] = cost + Math.min(
                        Math.min(dtw[i - 1][j] + deletionCost,   // deletion
                                 dtw[i][j - 1] + insertionCost), // insertion
                        dtw[i - 1][j - 1]                       // match/substitution
                );
            }
        }

        return dtw[n][m];
    }
}
