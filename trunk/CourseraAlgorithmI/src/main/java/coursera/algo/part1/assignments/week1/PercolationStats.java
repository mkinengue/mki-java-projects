package coursera.algo.part1.assignments.week1;

import coursera.algo.stdlib.StdIn;
import coursera.algo.stdlib.StdOut;

/**
 * @author MKINENGU<br />
 */

/**
 * Class for percolation statistics Computes T independent percolation on a NxN grid and measures the mean, standard
 * deviation and the confidence interval by giving its min and max
 */
public class PercolationStats {

	private final int T;

	private final double[] thresholds;

	private double mean;
	private double stdDev;

	/**
	 * Constructor which initializes T percolations (and make them percolate) with a grid of size N-by-N. Store all
	 * initialized percolations in a map with the corresponding number of run in the natural order from 1 to T Throws a
	 * IllegalArgumentException in case N or T is negative or equal to 0
	 * 
	 * @param N
	 * @param T
	 */
	public PercolationStats(final int N, final int T) {
		if ((N <= 0) || (T <= 0)) {
			throw new IllegalArgumentException("All the arguments given as inputs"
					+ " should always be strictly greater than 0");
		}

		this.T = T;

		thresholds = new double[T];

		// Initialize the percolation for each iteration and percolates until there
		// is one
		int cpt = 1;
		final int disp = T / 10;
		long debut = System.currentTimeMillis();
		final long start = debut;
		for (int i = 1; i <= T; i++) {
			final Percolation percolation = new Percolation(N);
			int percoNb = 0;
			while (!percolation.percolates()) {
				percoNb++;
			}

			final double average = (double) percoNb / (N * N);
			thresholds[i - 1] = average;

			if ((cpt % disp) == 0) {
				StdOut.println(cpt + " percolations done in " + (System.currentTimeMillis() - debut) + " ms");
				debut = System.currentTimeMillis();
			}
			cpt++;
		}
		StdOut.println("End : " + T + " percolations done in " + (System.currentTimeMillis() - start) + " ms");
	}

	/**
	 * Compute the mean of all thresholds
	 */
	public double mean() {
		if (mean == 0d) {
			double average = 0d;
			for (final double threshold : thresholds) {
				average += threshold;
			}
			mean = (average / T);
		}
		return mean;
	}

	/**
	 * Compute the standard deviation for this experiment
	 */
	public double stddev() {
		if (T == 1) {
			// Standard deviation cannot be computed, value to NAN
			stdDev = Double.NaN;
		} else if (stdDev == 0d) {
			double variance = 0d;
			for (final double threshold : thresholds) {
				variance += (threshold - mean()) * (threshold - mean());
			}
			stdDev = variance / (T - 1);
			stdDev = Math.sqrt(stdDev);
		}
		return stdDev;
	}

	/**
	 * Compute and return the lower bound of the 95% confidence interval
	 */
	public double confidenceLo() {
		return (mean() - (1.96d * stdDev / Math.sqrt(T)));
	}

	/**
	 * Compute and return the upper bound of the 95% confidence interval
	 */
	public double confidenceHi() {
		return (mean() + (1.96d * stdDev / Math.sqrt(T)));
	}

	/**
	 * For testing purposes
	 */
	public static void main(final String[] args) {
		final int N = StdIn.readInt();
		final int T = StdIn.readInt();

		// Implementation of the stats
		final PercolationStats percoStats = new PercolationStats(N, T);

		// Display the stats
		StdOut.println("mean                    = " + percoStats.mean());
		StdOut.println("stddev                  = " + percoStats.stddev());
		StdOut.println("95% confidence interval = " + percoStats.confidenceLo() + ", " + percoStats.confidenceHi());
	}
}