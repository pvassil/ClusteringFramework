package dom;

/**
 * 
 * @author pvassil
 *
 */
public interface IClusterableObject {
    int getId();
    String getMetaInfo();
    Double[] getNumericalValues();  // may be null if not numeric
    String getStringValue();        // may be null if not string-based
    String toString();
}
