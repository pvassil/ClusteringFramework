package service.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import dom.DataCollection;
import dom.NumericObject;

/**
 * Utility class to load numeric vector data from a delimited file into a DataCollection.
 * The file is expected to have a header line, then one line per object:
 *   metaInfo<delimiter>value1<delimiter>value2<delimiter>...<valueN>
 */
public class DataNumericLoader implements IStructuredFileLoader{

    /**
     * Loads a file into a DataCollection of NumericObjects.
     *
     * @param filePath  the path to the input file
     * @param delimiter the delimiter separating meta-info and numeric values (e.g., tab, comma)
     * @return a DataCollection containing all NumericObjects
     * @throws IOException if reading the file fails
     * @throws NumberFormatException if a numeric value cannot be parsed
     */
	@Override
    public DataCollection load(String filePath, String delimiter) throws IOException {
        DataCollection collection = new DataCollection();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int idCounter = 1;

            // Skip header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(delimiter);
                if (parts.length < 2) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                String metaInfo = parts[0].trim();
                Double[] values = new Double[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    values[i - 1] = Double.parseDouble(parts[i].trim());
                }

                collection.add(new NumericObject(idCounter++, values, metaInfo));
            }
        }

        return collection;
    }
}
