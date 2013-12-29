package coursera.algo.part1.personal_implementation;

/**
 * Implementation of the selection sort type
 */
public class MySelectionSort {

	/**
	 * Sort the array usin the method called "Selection Sort"
	 * 
	 * @param a
	 */
	public static void sort(final Comparable<Object>[] a) {
		if (a == null) {
			throw new RuntimeException("The array cannot be null");
		}

		final int N = a.length;
		for (int i = 0; i < (N - 1); i++) {
			int idxMin = i;
			for (int j = (i + 1); j < N; j++) {
				// Look for the min index in the remaining array (from i+1 to the end of the array)
				if (MySortUtils.less(a[j], a[idxMin])) {
					idxMin = j;
				}
			}

			// Exchange in case the min index is different from i; that is, a value smaller than the one
			// in index i was found
			if (i != idxMin) {
				MySortUtils.exch(a, i, idxMin);
			}
		}
	}
}
