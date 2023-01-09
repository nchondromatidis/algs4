/**
 * Performs a series of computational experiments on percolation model.
 * Created by nikos on 2/10/14.
 */
public class PercolationStats {
    private double[] percolationExperiments;    // percolation experiment data
    private int T;                              // number of experiments

    /**
     * Perform T independent computational experiments on an N-by-N grid and stores results
     * in percolationExperiments array
     * @param T
     * @throws java.lang.IllegalArgumentException unless N>1 and T>1
     */
    public PercolationStats(int N, int T) {
        if (N <= 1) throw new IllegalArgumentException("N must be more than 1");
        if (T <= 1) throw new IllegalArgumentException("T must be more than 1");
        this.T = T;

        percolationExperiments = new double[T];

        for (int i = 1; i <= T; i++) {
            percolationExperiments[i-1] = percolationExperiment(N);
        }
    }

    private double percolationExperiment(int N) {
            int openSites = 0;
            Percolation percolation = new Percolation(N);
            while (!percolation.percolates()) {
                int randomi = StdRandom.uniform(1, N+1);
                int randomj = StdRandom.uniform(1, N+1);
                if (!percolation.isOpen(randomi, randomj)) {
                    percolation.open(randomi, randomj);
                    openSites++;
                }
            }
            return (double) openSites/(N*N);
    }

    /**
     * Sample mean of percolation threshold
     * @return the mean of percolation threshold from the experiments
     */
    public double mean() {
        return StdStats.mean(percolationExperiments);
    }

    /**
     * Sample standard deviation of percolation threshold
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(percolationExperiments);
    }

    /**
     * Computes the lower bound of the 95% confidence interval
     * @return lower bound of the 95% confidence interval
     */
    public double confidenceLo() {
        return (mean()-(1.96*stddev()/Math.sqrt(T)));
    }

    /**
     * Computes upper bound of the 95% confidence interval
     * @return upper bound of the 95% confidence interval
     */
    public double confidenceHi() {
        return (mean()+(1.96*stddev()/Math.sqrt(T)));
    }

    /**
     * Performs a series of computational experiments.
     * @param args command line arguments grid size, number of experiments
     */
    public static void main(String[] args) {
        int N = StdIn.readInt(); //size of the grid
        int T = StdIn.readInt(); //computational experiments

        //runs the experiment
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println(percolationStats.mean());
        StdOut.println(percolationStats.stddev());
        StdOut.println(percolationStats.confidenceLo()+" "+percolationStats.confidenceHi());
    }
}