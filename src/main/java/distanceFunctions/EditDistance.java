package distanceFunctions;

import dom.IClusterableObject;

public class EditDistance implements IStringDistanceFunction {

    private double insertCost;
    private double deleteCost;
    private double replaceCost;

    // Constructor with default cost = 1
    public EditDistance() {
        this(1.0, 1.0, 1.0);
    }

    public EditDistance(double insertCost, double deleteCost, double replaceCost) {
        this.insertCost = insertCost;
        this.deleteCost = deleteCost;
        this.replaceCost = replaceCost;
    }

    @Override
    public Double computeDistance(IClusterableObject o1, IClusterableObject o2) {
        String s1 = o1.getStringValue();
        String s2 = o2.getStringValue();
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("EditDistance requires string values");
        }
        return levenshtein(s1, s2);
    }

    private double levenshtein(String s1, String s2) {
        double[][] dp = new double[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i * deleteCost;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j * insertCost;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                double cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0.0 : replaceCost;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + deleteCost,    // deletion
                                 dp[i][j - 1] + insertCost),   // insertion
                        dp[i - 1][j - 1] + cost                 // substitution
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
