package service.loaders;

import org.junit.jupiter.api.Test;

import dom.DataCollection;
import dom.NumericObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DataLoaderTest {

    @Test
    public void testDataStringLoader() throws IOException {
        // Prepare temporary input file
        File tempFile = File.createTempFile("string_data_test", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("projectName\tLevel11Sgn\n"); // header
            writer.write("proj1\taa\n");
            writer.write("proj2\tab\n");
            writer.write("proj3\txyz\n");
        }

        // Load data
        DataStringLoader loader = new DataStringLoader();
        DataCollection collection = loader.load(tempFile.getAbsolutePath(), "\t");
        assertEquals(3, collection.getCollectionData().size());

        // Check metaInfo and stringValue
        assertEquals("proj1", collection.getCollectionData().get(0).getMetaInfo());
        assertEquals("aa", collection.getCollectionData().get(0).getStringValue());
    }

    @Test
    public void testDataNumericLoader() throws IOException {
        // Prepare temporary numeric input file
        File tempFile = File.createTempFile("numeric_data_test", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("projectName\tval1\tval2\n"); // header
            writer.write("proj1\t1.0\t2.0\n");
            writer.write("proj2\t3.5\t4.5\n");
            writer.write("proj3\t0.0\t-1.0\n");
        }

        // Load numeric data
        DataNumericLoader loader = new DataNumericLoader();
        DataCollection collection = loader.load(tempFile.getAbsolutePath(), "\t");
        assertEquals(3, collection.getCollectionData().size());

        Double[] vals = ((NumericObject) collection.getCollectionData().get(0)).getNumericalValues();
        assertArrayEquals(new Double[]{1.0, 2.0}, vals);
    }
}
