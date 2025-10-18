package result;

import java.util.ArrayList;
import java.util.List;

import distanceFunctions.IDistanceFunction;
import dom.DataCollection;

public class ClusterSet {
	private List<Cluster> clusters;
    private DataCollection data;
    private IDistanceFunction distanceFunction;
    private Silhouette silhouette;
    
    public ClusterSet(DataCollection data, IDistanceFunction distanceFunction) {
		super();
		this.clusters = new ArrayList<Cluster>();
		this.data = data;
		this.distanceFunction = distanceFunction;

		this.silhouette = new Silhouette(this.clusters, distanceFunction);
	}
    
    public void addCluster(Cluster cluster) {
        clusters.add(cluster);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public DataCollection getData() {
		return data;
	}

	public IDistanceFunction getDistanceFunction() {
		return distanceFunction;
	}

	public int size() {
        return clusters.size();
    }

	public double getSilhouetteValue() {
		return this.silhouette.getSilhouetteValue();
	}
	
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClusterSet:\n");
        for (Cluster c : clusters) {
            sb.append(c.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a flattened list of all member-to-cluster mappings
     * across all clusters in this set.
     */
    public List<String> getAllMemberToClusterMappings() {
        List<String> allMappings = new ArrayList<>();
        for (Cluster cluster : clusters) {
            allMappings.addAll(cluster.getMemberToClusterMappings());
        }
        return allMappings;
    }


}
