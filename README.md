# Clustering Framework in Java

# Clustering Framework

![Java](https://img.shields.io/badge/java-8%2B-blue)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
![Tests](https://img.shields.io/badge/tests-passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-90%25-yellowgreen)

A modular clustering framework in Java that supports multiple algorithms, distance functions, loaders, and result evaluation.
The architecture is cleanly separated into packages, following good design practices (Builder pattern, separation of concerns, extensibility).

---

## 📦 Package Structure

```

src/main/java
├── algorithms/           # Clustering algorithms and supporting structures
│   ├── IClusteringAlgorithm.java
│   ├── HierarchicalAgglomerativeClustering.java
│   ├── DBSCANClustering.java
│   ├── KMedoidsClustering.java
│   └── DistanceMatrix.java
│
├── distanceFunctions/    # Distance function interfaces and implementations
│   ├── IDistanceFunction.java
│   ├── EuclideanDistance.java
│   ├── EditDistance.java
│   └── DTWDistance.java
│
├── dom/                  # Data model
│   ├── DataCollection.java
│   ├── IClusterableObject.java
│   ├── StringObject.java
│   └── NumericObject.java
│
├── result/               # Results, evaluation, and metrics
│   ├── Cluster.java
│   ├── ClusterSet.java
│   └── Silhouette.java
│
├── service/              # High-level orchestration
│   ├── ClusteringManager.java
│   │
│   ├── exporters/               # Result exporters
│   │   └── DataClusterExporter.java
│   │
│   └── loaders/               # Data loaders
│       ├── DataStringLoader.java
│       └── DataNumericLoader.java

src/examples/java
│
└── *.java       # Example drivers and experiments


```

---

## 🚀 Features

- **Clustering Algorithms**
  - Hierarchical Agglomerative Clustering (HAC)
  - DBSCAN
  - K-Medoids

- **Distance Functions**
  - Euclidean
  - Edit Distance (Levenshtein)
  - Dynamic Time Warping (DTW)
  - Manhattan

- **Data Handling**
  - Unified `DataCollection` abstraction
  - Supports both **strings** and **numeric vectors**
  - Extensible `IClusterableObject` interface

- **Results & Evaluation**
  - Cluster + ClusterSet representation
  - Silhouette score for cluster quality
  - Export of member → cluster mappings

- **Infrastructure**
  - Builder-pattern based `ClusteringManager` for simple orchestration
  - File-based loaders (e.g., `DataStringLoader`)
  - Clean separation of packages for extensibility

---

## 📂 Example Input File

A `DataStringLoader` can load tab-separated files where:
1. The **first line** is a header (ignored).
2. Each subsequent line pertains to a single record and contains:
   - **meta-info** (e.g., name, unique identifier, etc.)
   - **string or numeric value**.

Example:

```

projectName    Level11Sgn
3ev\_\_tev\_label    b=±
AA-ALERT\_\_frbcatdb    =b±=±=
accgit\_\_acl    b±=
aimeos\_\_aimeos-typo3    =b=±=±=±=±=±

````

---

## 📝 Example Usage

```java
// Load data
DataCollection stringData = DataStringLoader.loadFromFile("data/input.tsv", "\t");

// Build manager
HashMap<String, String> params = new HashMap<>();
params.put("targetClusters", "3");

ClusteringManager manager = new ClusteringManager.Builder()
        .withData(stringData)
        .withAlgorithm(ClusteringManager.AlgorithmType.HAC)
        .withDistance(ClusteringManager.DistanceFunctionType.EDIT)
        .withParams(params)
        .withOutputFile("results/output.txt")
        .build();

// Run & export
ClusterSet clusters = manager.executeAndExport();
````

This will produce a text file where each line maps a **meta-info → cluster**.

---

## 🧪 Testing

* Each major component has **JUnit tests** under the same package.
* Extended tests for `ClusteringManager` validate both:

  * Cluster contents
  * Exported results

Run with:

```bash
mvn test
```

---

## 🌱 Extending the Framework

* Add a new algorithm → `algorithms/`
* Add a new distance function → `distanceFunctions/`
* Add a new data type → implement `IClusterableObject`
* Add a new loader → `loader/`

The `ClusteringManager` will integrate them automatically if enums and builder mappings are updated.

---

## 📊 Architecture Diagram

This project can be visualized with a **package diagram** (see `src/clustering.png`).
You can render it [here](https://plantuml.com/plantuml).

---

## 📜 License

MIT License – free to use, modify, and distribute.

---

## 🙌 🧑 ‍💻 Acknowledgments

* Inspired by clean software architecture principles
* Uses Joshua Bloch’s Builder Pattern for the `ClusteringManager`
* Designed with extensibility and research experimentation in mind
* ChatGPT did all the hard work


# ClusteringFramework
