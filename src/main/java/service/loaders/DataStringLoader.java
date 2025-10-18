package service.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import dom.DataCollection;
import dom.StringObject;

/**
 * Utility class to load string data from a delimited file into a DataCollection.
 * The file is expected to have a header line, then one line per object:
 *   metaInfo<TAB>stringValue
 */
public class DataStringLoader implements IStructuredFileLoader {

    /**
     * Loads a file into a DataCollection of StringObjects.
     *
     * @param filePath the path to the input file
     * @param delimiter the delimiter separating meta-info and string value (e.g., "\t")
     * @return a DataCollection containing all StringObjects
     * @throws IOException if reading the file fails
     */
	@Override
    public DataCollection load(String filePath, String delimiter) throws IOException {
        DataCollection collection = new DataCollection();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int idCounter = 1;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(delimiter, 2); // split into 2 parts only
                if (parts.length < 2) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }
                String metaInfo = parts[0].trim();
                String stringValue = parts[1].trim();

                collection.add(new StringObject(idCounter++, stringValue, metaInfo));
            }
        }

        return collection;
    }
}
