package service.loaders;

import java.io.IOException;

import dom.DataCollection;

public interface IStructuredFileLoader {

    /**
     * Loads a file into a DataCollection of IClusterableObjects.
     * The file must be a text file, each row pertaining to a record, attributes delimited by delimiter.
     * The first column becomes the metadata for each record of the data collection,
     * and the second column becomes the actual value of the record.
     *  
     * @param filePath  the path to the input file
     * @param delimiter the delimiter separating meta-info and numeric values (e.g., tab, comma)
     * @return a DataCollection containing all NumericObjects
     * @throws IOException if reading the file fails
     * @throws NumberFormatException if a correct value cannot be parsed
     */
    public abstract DataCollection load(String filePath, String delimiter) throws IOException;
}