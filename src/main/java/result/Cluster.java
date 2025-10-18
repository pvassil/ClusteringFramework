package result;

import java.util.ArrayList;
import java.util.List;

import dom.IClusterableObject;

public class Cluster {
    private int id;
    private List<IClusterableObject> members;
//TODO cohesion, separation
    
    public Cluster(int id) {
        this.id = id;
        this.members = new ArrayList<IClusterableObject>();
    }

    public int getId() {
        return id;
    }

    public void addMember(IClusterableObject obj) {
        members.add(obj);
    }

    public List<IClusterableObject> getMembers() {
        return members;
    }

    public int size() {
        return members.size();
    }

    /**
     * Returns a list of strings, each representing a mapping of 
     * an object's meta information to this cluster's id.
     * Example: "schema1 -> Cluster_1"
     */
    public List<String> getMemberToClusterMappings() {
        List<String> mappings = new ArrayList<String>();
        for (IClusterableObject obj : members) {
            mappings.add(obj.getMetaInfo() + "\t" + this.getId());
        }
        return mappings;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cluster ").append(id).append(": ");
        for (IClusterableObject obj : members) {
            sb.append(obj.getMetaInfo()).append(" ");
        }
        return sb.toString();
    }
}
