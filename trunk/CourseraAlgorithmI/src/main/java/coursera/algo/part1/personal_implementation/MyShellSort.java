package coursera.algo.part1.personal_implementation;

/**
 * Implementation of the Shell sort type in ascending order
 */
public class MyShellSort {

	/**
	 * Sort the array usin the method called "Shell Sort"
	 * 
	 * @param a
	 */
	public static void sort(final Comparable<Object>[] a) {
		if (a == null) {
			throw new RuntimeException("The array cannot be null");
		}

		final int N = a.length;

		// Compute the h-sort increment
		int h = 1;
		while (h < N / 3) {
			h = 3 * h + 1;
		}

		while (h >= 1) {
			for (int i = h; i < N; i++) {
				for (int j = i; j >= h && MySortUtils.less(a[j], a[j - h]); j -= h) {
					// Element in position j is greater than current min element : exchange them
					MySortUtils.exch(a, j, j - h);
				}
			}
			h = h / 3;
		}
	}
}
