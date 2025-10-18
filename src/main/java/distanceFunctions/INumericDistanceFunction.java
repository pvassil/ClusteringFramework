package distanceFunctions;

import dom.IClusterableObject;

public interface INumericDistanceFunction extends IDistanceFunction {
    @Override
    Double computeDistance(IClusterableObject o1, IClusterableObject o2);
}
