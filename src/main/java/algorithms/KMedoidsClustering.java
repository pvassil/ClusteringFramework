package algorithms;

import java.util.*;

import distanceFunctions.IDistanceFunction;
import dom.DataCollection;
import dom.IClusterableObject;
import result.Cluster;
import result.ClusterSet;

/**
 * K-Medoids clustering implementation.
 *
 * Parameters:
 *  - "k" (required): number of clusters
 *  - "randomSeed" (optional): seed for reproducibility (default = System.currentTimeMillis())
 *  - "maxIterations" (optional): maximum number of iterations per run (default = 100)
 *  - "numRuns" (optional): number of random restarts, best clustering is returned (default = 1)
 *  - "verbose" (optional): "true" or "false" to enable logging (default = false)
 */
public class KMedoidsClustering implements IClusteringAlgorithm {

    @Override
    public ClusterSet computeClusterSet(DataCollection data, IDistanceFunction distanceFunction,
                                        HashMap<String, String> parameters) {
        int k = Integer.parseInt(parameters.getOrDefault("k", "2"));
        int maxIterations = Integer.parseInt(parameters.getOrDefault("maxIterations", "100"));
        int numRuns = Integer.parseInt(parameters.getOrDefault("numRuns", "1"));
        boolean verbose = Boolean.parseBoolean(parameters.getOrDefault("verbose", "false"));

        long seed = parameters.containsKey("randomSeed")
                ? Long.parseLong(parameters.get("randomSeed"))
                : System.currentTimeMillis();

        Random random = new Random(seed);

        ClusterSet bestClusterSet = null;
        double bestCost = Double.MAX_VALUE;

        for (int run = 0; run < numRuns; run++) {
            if (verbose) {
                System.out.println("Run " + (run + 1) + "/" + numRuns);
            }

            // === Step 1: Initialize medoids randomly ===
            List<IClusterableObject> objects = new ArrayList<>(data.getCollectionData());
            Collections.shuffle(objects, random);
            List<IClusterableObject> medoids = new ArrayList<>(objects.subList(0, k));

            boolean changed = true;
            int iterations = 0;

            while (changed && iterations < maxIterations) {
                iterations++;

                // === Step 2: Assign points to nearest medoid ===
                Map<IClusterableObject, List<IClusterableObject>> clusters = new HashMap<>();
                for (IClusterableObject medoid : medoids) {
                    clusters.put(medoid, new ArrayList<>());
                }

                for (IClusterableObject obj : objects) {
                    IClusterableObject bestMedoid = null;
                    double bestDistance = Double.MAX_VALUE;

                    for (IClusterableObject medoid : medoids) {
                        double d = distanceFunction.computeDistance(obj, medoid);
                        if (d < bestDistance) {
                            bestDistance = d;
                            bestMedoid = medoid;
                        }
                    }
                    clusters.get(bestMedoid).add(obj);
                }

                // === Step 3: Try swapping medoids with non-medoids ===
                changed = false;
                for (IClusterableObject currentMedoid : new ArrayList<>(medoids)) {
                    for (IClusterableObject candidate : objects) {
                        if (medoids.contains(candidate)) continue;

                        List<IClusterableObject> newMedoids = new ArrayList<>(medoids);
                        newMedoids.remove(currentMedoid);
                        newMedoids.add(candidate);

                        double currentCost = totalCost(objects, medoids, distanceFunction);
                        double newCost = totalCost(objects, newMedoids, distanceFunction);

                        if (newCost < currentCost) {
                            medoids = newMedoids;
                            changed = true;
                            if (verbose) {
                                System.out.println("Iteration " + iterations + ": Improved cost from "
                                        + currentCost + " to " + newCost);
                            }
                        }
                    }
                }
            }

            double runCost = totalCost(objects, medoids, distanceFunction);
            if (runCost < bestCost) {
                bestCost = runCost;
                bestClusterSet = buildClusterSet(medoids, objects, data, distanceFunction);
            }
        }

        return bestClusterSet;
    }

    private double totalCost(List<IClusterableObject> objects, List<IClusterableObject> medoids,
                             IDistanceFunction distanceFunction) {
        double cost = 0.0;
        for (IClusterableObject obj : objects) {
            double bestDist = Double.MAX_VALUE;
            for (IClusterableObject medoid : medoids) {
                bestDist = Math.min(bestDist, distanceFunction.computeDistance(obj, medoid));
            }
            cost += bestDist;
        }
        return cost;
    }

    private ClusterSet buildClusterSet(List<IClusterableObject> medoids, List<IClusterableObject> objects, DataCollection data,
                                       IDistanceFunction distanceFunction) {
        ClusterSet set = new ClusterSet(data, distanceFunction);
        Map<IClusterableObject, Cluster> clusterMap = new HashMap<>();

        int clusterId = 1;
        for (IClusterableObject medoid : medoids) {
            Cluster c = new Cluster(clusterId++);
            clusterMap.put(medoid, c);
            set.addCluster(c);
        }

        for (IClusterableObject obj : objects) {
            IClusterableObject bestMedoid = null;
            double bestDist = Double.MAX_VALUE;
            for (IClusterableObject medoid : medoids) {
                double d = distanceFunction.computeDistance(obj, medoid);
                if (d < bestDist) {
                    bestDist = d;
                    bestMedoid = medoid;
                }
            }
            clusterMap.get(bestMedoid).addMember(obj);
        }

        return set;
    }
}
