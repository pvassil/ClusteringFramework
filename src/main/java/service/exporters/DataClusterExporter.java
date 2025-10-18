package service.exporters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import result.ClusterSet;

public class DataClusterExporter {

	/**
	 * Exports a result file with one row per record, having the record metadata and its cluster
	 * 
	 * @param results a ClusterSet with the results of the output clustering
	 * @param outputFilePath a String with the path of the output file
	 * @return the number of lines written in the outputFile
	 * @throws IOException
	 */
	public static int export(ClusterSet results, String outputFilePath) throws IOException {
		List<String> mappings = results.getAllMemberToClusterMappings();

		int counter = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String line : mappings) {
                writer.write(line);
                writer.newLine();
                counter++;
            }
        }
        System.out.println("Clustering results exported to: " + outputFilePath);
        return counter;
	}
}
