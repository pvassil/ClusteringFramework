package dom;

/**
 * Wrapper for numeric objects to implement IClusterableObject.
 * Each object has an ID, a numerical vector, and optional metadata.
 */
public class NumericObject implements IClusterableObject {

    private int id;
    private Double[] values;
    private String metaInfo;

    /**
     * Constructor with all required fields.
     *
     * @param id Unique identifier for the object.
     * @param values Numerical values representing the object.
     * @param metaInfo Optional metadata (e.g., name or description).
     */
    public NumericObject(int id, Double[] values, String metaInfo) {
        this.id = id;
        this.values = values;
        this.metaInfo = metaInfo;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getMetaInfo() {
        return metaInfo;
    }

    @Override
    public Double[] getNumericalValues() {
        return values;
    }

    @Override
    public String getStringValue() {
        return null; // Not applicable for numeric objects
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("NumericObject{id=" + id + ", values=[");
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i < values.length - 1) sb.append(", ");
            }
        }
        sb.append("], metaInfo=").append(metaInfo).append("}");
        return sb.toString();
    }
}

