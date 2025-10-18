package dom;

public class StringObject implements IClusterableObject {
    private int id;
    private String value;
    private String meta;

    public StringObject(int id, String value, String meta) {
        this.id = id;
        this.value = value;
        this.meta = meta;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getMetaInfo() { return meta; }

    @Override
    public Double[] getNumericalValues() { return null; }

    @Override
    public String getStringValue() { return value; }

    @Override
    public String toString() {
        return "StringObject{id=" + id + ", value='" + value + "', meta='" + meta + "'}";
    }
}
