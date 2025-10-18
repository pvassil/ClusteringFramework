package distanceFunctions;

import dom.IClusterableObject;

/**
 * Computes Euclidean distance between numeric objects.
 * Works with objects implementing getNumericalValues().
 */
public class EuclideanDistance implements INumericDistanceFunction {

    @Override
    public Double computeDistance(IClusterableObject object1, IClusterableObject object2) {
        if (object1 == null || object2 == null) {
            throw new IllegalArgumentException("Objects cannot be null");
        }

        Double[] vals1 = object1.getNumericalValues();
        Double[] vals2 = object2.getNumericalValues();

        if (vals1 == null || vals2 == null || vals1.length != vals2.length) {
            throw new IllegalArgumentException("Numerical values must be non-null and of equal length");
        }

        double sum = 0.0;
        for (int i = 0; i < vals1.length; i++) {
            double diff = vals1[i] - vals2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
