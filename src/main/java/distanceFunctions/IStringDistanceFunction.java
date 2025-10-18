package distanceFunctions;

import dom.IClusterableObject;

public interface IStringDistanceFunction extends IDistanceFunction {
    @Override
    Double computeDistance(IClusterableObject o1, IClusterableObject o2);
}
