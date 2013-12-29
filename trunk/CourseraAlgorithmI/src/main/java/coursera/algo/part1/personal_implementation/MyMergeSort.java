package coursera.algo.part1.personal_implementation;

public class MyMergeSort {

	private static final int CUTOFF = 7;

	@SuppressWarnings("unchecked")
	public static void sort(final Comparable<Object>[] a) {
		if ((a == null) || (a.length == 0)) {
			return;
		}

		// Create the auxiliary array
		final Comparable<Object>[] aux = new Comparable[a.length];
		sort(a, aux, 0, a.length - 1);
	}

	/**
	 * 
	 * @param a
	 * @param aux
	 * @param lo
	 * @param hi
	 */
	private static void sort(final Comparable<Object>[] a, final Comparable<Object>[] aux, final int lo, final int hi) {
		if (hi <= (CUTOFF + lo - 1)) {
			MyInsertionSort.sort(a);
			return;
		}

		// if (hi <= lo) {
		// return;
		// }

		final int mid = (lo + hi) / 2;
		sort(a, aux, lo, mid);
		sort(a, aux, mid + 1, hi);

		// In case the first element of the second part is already greater than or equal to the last
		// one of the first part, there is no need to merge both parts , because it means, they are already
		// sorted
		if (!MySortUtils.less(a[mid + 1], a[mid])) {
			return;
		}

		MySortUtils.merge(a, aux, lo, mid, hi);
	}
}
