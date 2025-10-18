package result;

import java.util.List;

import distanceFunctions.IDistanceFunction;
import dom.IClusterableObject;

public class Silhouette {
	
	private double silhouetteValue = Double.MAX_VALUE;
	private List<Cluster> clusters;
	private IDistanceFunction distanceFunction;
	
	public Silhouette(List<Cluster> clusters, IDistanceFunction distanceFunction){
		this.clusters=clusters;
		this.distanceFunction = distanceFunction;
	}
	
	private double computeGlobalSilhouetteValue(){
		double allSilhouettes=0.0;
		int countObjects=0;
		
		for(int i=0;i<clusters.size();i++){
			Cluster currentCluster = clusters.get(i);
			List<IClusterableObject> currentClusterMembers = currentCluster.getMembers();
			countObjects += currentClusterMembers.size();
			for(int k=0;k<currentClusterMembers.size();k++){	
				IClusterableObject currentObjectIK = currentClusterMembers.get(k);
				double silhouetteIK=0.0;
				double ai=computeA(currentObjectIK,currentCluster);
				double bi=computeB(currentObjectIK,currentCluster);
			
				if(currentClusterMembers.size()==1){
					silhouetteIK=0.0;
				}
				else{
					double maxValue=0.0;
					if(bi>ai){
						maxValue=bi;
						silhouetteIK=(bi-ai)/maxValue;
					}
					else if (ai>bi){
						maxValue=ai;
						silhouetteIK=(bi-ai)/maxValue;
					}
					else{
						silhouetteIK=0.0;
					}
				}
				allSilhouettes+=silhouetteIK;
			}
		}
		
		this.silhouetteValue=allSilhouettes/countObjects;
		return this.silhouetteValue;
	}

	private double computeA(IClusterableObject currentObject, Cluster currentCluster){
		double sum=0.0;
		double ai=0.0;
		List<IClusterableObject> clusterMembers = currentCluster.getMembers();
		int cardinality = clusterMembers.size(); if(0 == cardinality) return 0.0;
		for(int j=0;j<cardinality;j++){
			sum+=this.distanceFunction.computeDistance(currentObject, clusterMembers.get(j));
		}
		ai=sum/cardinality;
		return ai;
	}
	
	private double computeB(IClusterableObject currentObject, Cluster currentCluster){
		double currentMinAvgDistanceToAllOtherClusters=Double.MAX_VALUE;
		
		for(int j=0;j<clusters.size();j++){
			Cluster otherCluster = clusters.get(j);
			if(otherCluster.equals(currentCluster)) 
				continue;
			List<IClusterableObject> otherClusterMembers = otherCluster.getMembers();
			int otherClusterSize = otherClusterMembers.size();
			if(0 == otherClusterSize) 
				continue;
			double avgDistanceOfOtherCluster=Double.MAX_VALUE;
			double sum=0.0;
			for(int l=0;l<otherClusterMembers.size();l++){
				sum += this.distanceFunction.computeDistance(currentObject, otherClusterMembers.get(l));
			}
			avgDistanceOfOtherCluster=sum/otherClusterSize;
			if(avgDistanceOfOtherCluster<currentMinAvgDistanceToAllOtherClusters){
				currentMinAvgDistanceToAllOtherClusters=avgDistanceOfOtherCluster;
			}
		}
		
		return currentMinAvgDistanceToAllOtherClusters;
	}
	
	public double getSilhouetteValue(){
		if(Double.MAX_VALUE == this.silhouetteValue)
			return this.computeGlobalSilhouetteValue(); 
		return this.silhouetteValue;
	}
	
}//end class
