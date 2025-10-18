package service;

//import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
//import java.util.List;

import algorithms.DBSCANClustering;
import algorithms.HierarchicalAgglomerativeClustering;
import algorithms.IClusteringAlgorithm;
import algorithms.KMeansClustering;
import algorithms.KMedoidsClustering;
import distanceFunctions.DTWDistance;
import distanceFunctions.EditDistance;
import distanceFunctions.EuclideanDistance;
import distanceFunctions.IDistanceFunction;
import distanceFunctions.ManhattanDistance;
import dom.DataCollection;
import dom.NumericObject;
import result.ClusterSet;
import service.exporters.DataClusterExporter;
import service.loaders.DataNumericLoader;
import service.loaders.DataStringLoader;
import service.loaders.IStructuredFileLoader;

//import java.io.FileWriter;
//import java.io.IOException;

/**
 * Facade class to manage clustering workflows.
 * <p>
 * Supports both string and numeric data. Uses the Builder pattern with
 * early validation of parameters before constructing the manager.
 *
 *
 * Example usage of {@link ClusteringManager} with the builder pattern:
 *
 * <pre>
 * {@code
 * // Prepare data
 * DataCollection stringData = new DataCollection();
 * stringData.add(new StringObject(1, "abc", "s1"));
 * stringData.add(new StringObject(2, "abd", "s2"));
 *
 * // Define algorithm parameters
 * HashMap<String,String> params = new HashMap<>();
 * params.put("targetClusters", "2");  // For HAC, DBSCAN may use eps/minPts, KMeans/KMedoids use k
 *
 * // Build the manager
 * ClusteringManager manager = new ClusteringManager.Builder()
 *         .withData(stringData)
 *         .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
 *         .withDistance(ClusteringManager.DistanceFunctionType.DTW)
 *         .withParams(params)
 *         .withOutputFile("clusters.txt") // optional, can be null
 *         .build();
 *
 * // Execute clustering
 * ClusterSet clusters = manager.execute();
 *
 * // Access clusters
 * System.out.println(clusters);
 * }
 * </pre>
 *
 * Notes:
 * - The builder ensures early validation of algorithm and distance function compatibility.
 * - For string sequences, use EDIT or DTW distances; for numeric vectors, use EUCLIDEAN.
 * - The output file is optional; if provided, clustering results are written as text.
 */
public class ClusteringManager {

    // --- Enums ---
	public enum DatasetType {STRING, NUMERIC}
    public enum AlgorithmType { HAC, KMEANS, KMEDOIDS, DBSCAN }
    public enum DistanceFunctionType { EDIT, DTW, EUCLIDEAN, MANHATTAN }

    // --- Attributes ---
    private final DataCollection data;
    private final HashMap<String, String> algorithmParams;
    private final String outputFilePath;
	private final String inputFilePath;
	private final String delimiter;
	private final IStructuredFileLoader loader;

    private final IClusteringAlgorithm algorithm;
    private final IDistanceFunction distanceFunction;

    // --- Private constructor ---
    private ClusteringManager(Builder builder) {
        this.data = builder.data;
        this.algorithmParams = builder.algorithmParams;
        this.outputFilePath = builder.outputFilePath;
    	this.inputFilePath = builder.inputFilePath;
    	this.loader = builder.loader;
    	this.delimiter = builder.delimiter;

    	// Construct actual algorithm and distance function objects
        this.algorithm = buildAlgorithm(builder.algorithmType);
        this.distanceFunction = buildDistanceFunction(builder.distanceFunctionType);
    }

    // --- Public facade method ---
    public ClusterSet execute() {
    	if(data==null)
    		try{
    			loader.load(inputFilePath, this.delimiter);
    		}
    	catch(IOException exception) {
    		System.err.println("Failed to open the input file:\n\t" + this.inputFilePath + "\n" + exception.toString());
    	}
    	
        ClusterSet clusters = algorithm.computeClusterSet(data, distanceFunction, algorithmParams);
        return clusters;
    }
   
    /**
     * Executes the clustering and writes the results to a file.
     *
     * @param filePath the path where results will be written
     * @return the produced ClusterSet
     * @throws IOException if writing to file fails
     */
    public ClusterSet executeAndExport() throws IOException {
    	if (this.outputFilePath == null) {
    	    throw new IllegalStateException("Output file path is not set in the manager.");
    	}
    	
    	ClusterSet results = execute();
    	try {
    		DataClusterExporter.export(results, this.outputFilePath);
    	} 
    	catch (IOException exception) {
    		System.err.println("Failed to write an output file.\n" + exception.toString());
    	}
        //export(results);

        return results;
    }

//	/**
//	 * @param results
//	 * @throws IOException
//	 */
//	private void export(ClusterSet results) throws IOException {
//		List<String> mappings = results.getAllMemberToClusterMappings();
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFilePath))) {
//            for (String line : mappings) {
//                writer.write(line);
//                writer.newLine();
//            }
//        }
//        System.out.println("Clustering results exported to: " + this.outputFilePath);
//	}

    // --- Private helper methods ---
    private IClusteringAlgorithm buildAlgorithm(AlgorithmType type) {
        switch (type) {
            case HAC: return new HierarchicalAgglomerativeClustering();
            case KMEANS: return new KMeansClustering();
            case KMEDOIDS: return new KMedoidsClustering();
            case DBSCAN: return new DBSCANClustering();
            default: throw new IllegalArgumentException("Unknown algorithm type: " + type);
        }
    }

    private IDistanceFunction buildDistanceFunction(DistanceFunctionType type) {
        switch (type) {
            case EDIT: return new EditDistance();
            case DTW: return new DTWDistance();
            case EUCLIDEAN: return new EuclideanDistance();
            case MANHATTAN: return new ManhattanDistance();
            default: throw new IllegalArgumentException("Unknown distance function type: " + type);
        }
    }

    // --- Builder ---
    public static class Builder {
        private DataCollection data = null;
    	private DatasetType datasetType = null;
    	private String inputFilePath = null ;
    	private String delimiter = null;
    	private IStructuredFileLoader loader = null;
        private AlgorithmType algorithmType = null;
        private DistanceFunctionType distanceFunctionType = null;
        private HashMap<String, String> algorithmParams = new HashMap<>();
        private String outputFilePath = null;

        public Builder withData(DataCollection data) {
            this.data = data;
            return this;
        }

        public Builder withDatasetType(DatasetType datasetType) {
            this.datasetType = datasetType;
            return this;
        }
        
        public Builder withInputFilePath(String inputFilePath) {
            this.inputFilePath = inputFilePath;
            return this;
        }

        public Builder withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }
        public Builder withAlgorithm(AlgorithmType algo) {
            this.algorithmType = algo;
            return this;
        }

        public Builder withDistance(DistanceFunctionType distance) {
            this.distanceFunctionType = distance;
            return this;
        }

        public Builder withParams(HashMap<String, String> params) {
            this.algorithmParams = params;
            return this;
        }

        public Builder withOutputFile(String path) {
            this.outputFilePath = path;
            return this;
        }

        /**
         * Validates parameters and constructs the ClusteringManager.
         *
         * @return ClusteringManager instance
         * @throws IllegalStateException if required parameters are missing
         * @throws IllegalArgumentException if distance-algorithm compatibility fails
         */
        public ClusteringManager build() {
            validate();
            return new ClusteringManager(this);
        }

        // --- Private validation ---
        private void validate() {
        	if ((datasetType == null) && (inputFilePath != null)) throw 
        		new IllegalStateException("The input file must be specified both wrt to path and filetype.");
        	if ((datasetType != null) && (inputFilePath == null)) throw 
        		new IllegalStateException("The input file must be specified both wrt to path and filetype.");
        	if ((datasetType != null) && (inputFilePath == null)&&(data == null)) throw 
        		new IllegalStateException("If no input file is given, at least a DataCollection must be provided.");
            if (algorithmType == null) throw new IllegalStateException("AlgorithmType must be provided.");
            if (distanceFunctionType == null) throw new IllegalStateException("DistanceFunctionType must be provided.");

            //delimiter
    		if(this.delimiter == null || this.delimiter.length()==0)
    			this.delimiter = "\t"; 
            // new DataCollection if file is given
            if ((datasetType != null) && (inputFilePath != null)) {
            	if(datasetType == DatasetType.STRING)
            		this.loader = new DataStringLoader();
            	else if(datasetType == DatasetType.NUMERIC)
            		this.loader = new DataNumericLoader();
            }
            
            // COMPATIBILITY CHECKS
            boolean hasNumeric = false;
            if (datasetType == DatasetType.NUMERIC)
            	hasNumeric = true;
            else if (data!=null)
            	hasNumeric = data.getCollectionData().stream().anyMatch(obj -> obj instanceof NumericObject);
            if ((distanceFunctionType == DistanceFunctionType.EUCLIDEAN) && !hasNumeric) {
                throw new IllegalArgumentException("EuclideanDistance requires numeric data.");
            }
            if ((distanceFunctionType == DistanceFunctionType.EDIT || distanceFunctionType == DistanceFunctionType.DTW)
                    && hasNumeric) {
                throw new IllegalArgumentException("EditDistance/DTWDistance requires string data.");
            }
        }
    }
}
