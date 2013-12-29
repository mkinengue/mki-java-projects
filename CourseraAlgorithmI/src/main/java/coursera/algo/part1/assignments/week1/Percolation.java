/**
 * 
 */
package coursera.algo.part1.assignments.week1;

import coursera.algo.stdlib.StdOut;
import coursera.algo.stdlib.StdRandom;
import coursera.algo.algs4.WeightedQuickUnionUF;


/**
 * @author MKINENGU<br />
 *         Percolation class implementation
 */
public class Percolation {

	/** 2D Grid containing whether a site is open or not */
	private final boolean[][] grid;

	/** Size of the squared grid */
	private final int size;

	/** Virtual top site added for development purpose */
	private final int virtualTopSite;

	/** Virtual bottom site added for development purpose */
	private final int virtualBottomSite;

	/** Weighted quick union structure to quickly find a percolation */
	private final WeightedQuickUnionUF wquf;

	/**
	 * Constructor initializing the N-by-N grid
	 * 
	 * @param N
	 */
	public Percolation(final int N) {
		if (N <= 0) {
			throw new IllegalArgumentException("The argument given as input "
					+ "should always be strictly greater than 0");
		}
		size = N;
		grid = new boolean[N][N];

		// Initialization of the Weighted Quick Union Find tool
		// Number of all elements of the grid including the virtual top and bottom
		// sites
		wquf = new WeightedQuickUnionUF(N * N + 2);

		// Compute virtual top site and virtual bottom site
		virtualTopSite = 0;
		virtualBottomSite = N * N + 1;

		// Connect the top sites of the grid to the virtual top in the weighted quick
		// union find tool
		for (int j = 1; j <= N; j++) {
			final int p = xyTo1D(1, j);
			wquf.union(virtualTopSite, p);
		}

		// Connect the bottom sites of the grid to the virtual bottom in the weighted
		// quick union find tool
		for (int j = 1; j <= N; j++) {
			final int p = xyTo1D(N, j);
			wquf.union(virtualBottomSite, p);
		}
	}

	/**
	 * Converts 2D coordinates (i, j) to an int value using the formula : (size * i) + j - size
	 * 
	 * @param i
	 * @param j
	 */
	private int xyTo1D(final int i, final int j) {
		checkIndices(i, j);
		return ((size * i) + j - size);
	}

	/**
	 * Check that the indices given as input are both valued between 1 and the variable size. Otherwise throw an
	 * IndexOutOfBoundsException exception
	 * 
	 * @param i
	 * @param j
	 */
	private void checkIndices(final int i, final int j) {
		if ((i < 1) || (i > size)) {
			throw new IndexOutOfBoundsException("Row indice out of range.");
		}
		if ((j < 1) || (j > size)) {
			throw new IndexOutOfBoundsException("Column indice out of range.");
		}
	}

	/**
	 * Open the site located in row i (1-N), column j (1-N) by uniting it with its neighbors (up, down, left and right)<br />
	 * All opened sites of the same row will be connected together Throw the exception IndexOutOfBoundsException in case
	 * either i or j is outside range
	 * 
	 * @param i
	 * @param j
	 */
	public void open(final int i, final int j) {
		checkIndices(i, j);
		if (!grid[i - 1][j - 1]) {
			// The site is not open yet : open it and connect it to its open site
			// neighbors
			grid[i - 1][j - 1] = true;

			// Get current site 1d conversion
			final int p = xyTo1D(i, j);

			// Try connecting current site to its left neighbor
			connectToOpenNeighbor(p, i, j - 1);

			// Try connecting current site to its bottom neighbor
			connectToOpenNeighbor(p, i + 1, j);

			// Try connecting current site to its right neighbor
			connectToOpenNeighbor(p, i, j + 1);

			// Try connecting current site to its top neighbor
			connectToOpenNeighbor(p, i - 1, j);
		}
	}

	/**
	 * Connect the site represented by its 1D coordinate p to its neighbor given by rowNeighbor and colNeighbor iff the
	 * indices are valid and the site is open
	 * 
	 * @param p
	 * @param rowNeighbor
	 * @param colNeighbor
	 */
	private void connectToOpenNeighbor(final int p, final int rowNeighbor, final int colNeighbor) {
		if (p < 1 || p > (size * size)) {
			throw new IndexOutOfBoundsException("Site represented with value " + p + " does not exist");
		} else if (rowNeighbor < 1 || rowNeighbor > size || colNeighbor < 1 || colNeighbor > size) {
			// Given neighbor indices do not exist
			return;
		} else if (!isOpen(rowNeighbor, colNeighbor)) {
			// Given neighbor is not an open site
			return;
		}
		wquf.union(p, xyTo1D(rowNeighbor, colNeighbor));
	}

	/**
	 * Return true iff the site with index (i-1, j-1) in the array is opened.<br />
	 * The check consists on verifying whether or not the site is set to true in the grid
	 * 
	 * @param i
	 * @param j
	 * @return true, false
	 */
	public boolean isOpen(final int i, final int j) {
		checkIndices(i, j);
		return grid[i - 1][j - 1];
	}

	/**
	 * Return true in case the site given by row i and column j is a full site type one : that is, the site is connected
	 * by its neighbors to a top site<br />
	 * Here the check consist on verifying if site given by (i,j) is connected to the virtual top site Return false
	 * otherwise
	 * 
	 * @param i
	 * @param j
	 * @return true, false
	 */
	public boolean isFull(final int i, final int j) {
		checkIndices(i, j);
		return isOpen(i, j) && wquf.connected(virtualTopSite, xyTo1D(i, j));
	}

	/**
	 * Check if this percolates by choosing randomly a site, opening it if not already opened and by verifying if the
	 * system percolates
	 */
	public boolean percolates() {
		// First, we check if the grid percolates already. If so return true
		final boolean percolates = wquf.connected(virtualBottomSite, virtualTopSite);
		if (percolates) {
			return true;
		}

		// Choose randomly the row and column indexes for a site between 1 and size
		int rowRand = StdRandom.uniform(size) + 1;
		int colRand = StdRandom.uniform(size) + 1;

		while (isOpen(rowRand, colRand)) {
			// Generate random row indice and random column indice until we find a
			// site not already open
			rowRand = StdRandom.uniform(size) + 1;
			colRand = StdRandom.uniform(size) + 1;
		}

		// Open current site since it is not opened yet
		open(rowRand, colRand);
		return wquf.connected(virtualBottomSite, virtualTopSite);
	}

	/**
	 * For testing purpose
	 */
	public static void main(final String[] args) {
		final Percolation perco = new Percolation(6);
		// StdOut.println("percolates = " + perco.percolates());
		// StdOut.println("isFull(1, 1) = " + perco.isFull(1, 1));
		perco.open(1, 1);
		StdOut.println("isFull(1, 1) = " + perco.isFull(1, 1));
		StdOut.println("isOpen(1, 1) = " + perco.isOpen(1, 1));
		// StdOut.println("percolates = " + perco.percolates());

		// perco.open(3, 2);
		StdOut.println("isFull(3, 2) = " + perco.isFull(3, 2));
		StdOut.println("isOpen(3, 2) = " + perco.isOpen(3, 2));
		// StdOut.println("percolates = " + perco.percolates());
		/*
		 * perco.open(2, 1); StdOut.println("isFull(2, 1) = " + perco.isFull(2, 1)); StdOut.println("isOpen(2, 1) = " +
		 * perco.isOpen(2, 1)); StdOut.println("percolates = " + perco.percolates());
		 * 
		 * perco.open(2, 2); StdOut.println("isFull(2, 2) = " + perco.isFull(2, 2)); StdOut.println("isOpen(2, 2) = " +
		 * perco.isOpen(2, 2)); StdOut.println("percolates = " + perco.percolates());
		 * 
		 * 
		 * StdOut.println("Case 1 : isFull(3,2) " + perco.isFull(3, 2)); StdOut.println("Perco percolates ? " +
		 * perco.percolates());
		 */

		/***********************************************************/
		/*
		 * for (int i=0; i<100; i++) { int N = 20; perco = new Percolation(N); int cpt = 0; while (!perco.percolates())
		 * { cpt++; }
		 * 
		 * double threshold = ((double) cpt) / (N*N);
		 * 
		 * StdOut.println("cpt  = " + cpt); StdOut.println("percolation threshold  = " + threshold); StdOut.println("");
		 * }
		 */
	}
}