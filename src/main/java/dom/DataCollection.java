
package dom;

import java.util.ArrayList;
import java.util.List;

public class DataCollection {
    private List<IClusterableObject> collectionData;

    public DataCollection() {
        this.collectionData = new ArrayList<IClusterableObject>();
    }

    public void add(IClusterableObject obj) {
        collectionData.add(obj);
    }

    public List<IClusterableObject> getCollectionData() {
        return collectionData;
    }

    public int size() {
        return collectionData.size();
    }

    public IClusterableObject get(int index) {
        return collectionData.get(index);
    }
    
}

