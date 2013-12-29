package coursera.algo.part1.personal_implementation;

/**
 * Implementation of the insertion sort type in ascending order
 */
public class MyInsertionSort {

	/**
	 * Sort the array using the method called "Insertion Sort"
	 * 
	 * @param a
	 */
	public static void sort(final Comparable<Object>[] a) {
		if (a == null) {
			throw new RuntimeException("The array cannot be null");
		}

		final int N = a.length;
		for (int i = 1; i < N; i++) {
			for (int j = i; j > 0; j--) {
				if (MySortUtils.less(a[j], a[j - 1])) {
					// Element in position j is strictly smaller than current min element : exchange them
					MySortUtils.exch(a, j, j - 1);
				} else {
					// Element in position j is greater than or equal to current min element. Exit the loop
					break;
				}
			}
		}
	}
}
