package coursera.algo.part1.personal_implementation;

/**
 * Utils for sort type classes
 */
public final class MySortUtils {

	/**
	 * Private constructor
	 */
	private MySortUtils() {
	}

	/**
	 * Return true in case el1 is strictly smaller than el2<br />
	 * Return false otherwise<br />
	 * Throws a RuntimeException in case one of the arguments is null
	 * 
	 * @param el1
	 * @param el2
	 * @return true/false
	 */
	public static boolean less(final Comparable<Object> el1, final Comparable<Object> el2) {
		if ((el1 == null) || (el2 == null)) {
			throw new RuntimeException("Null parameters are not allowed.");
		}
		return (el1.compareTo(el2) < 0);
	}

	/**
	 * Return true in case el1 is strictly greater than el2<br />
	 * Return false otherwise<br />
	 * Throws a RuntimeException in case one of the arguments is null
	 * 
	 * @param el1
	 * @param el2
	 * @return true/false
	 */
	public static boolean greater(final Comparable<Object> el1, final Comparable<Object> el2) {
		if ((el1 == null) || (el2 == null)) {
			throw new RuntimeException("Null parameters are not allowed.");
		}
		return (el1.compareTo(el2) > 0);
	}

	/**
	 * Exchange both comparable Objects in indexes i and j.<br />
	 * Throws RuntimeException in case the array is null or if one of the indexes i and j is out of bounds of the array
	 * 
	 * @param array
	 * @param i
	 * @param j
	 */
	public static void exch(final Comparable<Object>[] array, final int i, final int j) {
		if (array == null) {
			throw new RuntimeException("The array cannot be null");
		} else if ((i < 0) || (i >= array.length) || (j < 0) || (j >= array.length)) {
			throw new RuntimeException("One of the indexes is out of bounds of the array");
		}

		final Comparable<Object> tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	/**
	 * Merge and sort array "a" by merging the sorted elements of part from lo to mid with the sorted elements of part
	 * from mid+1 to hi<br />
	 * Note that the elements are merged such that the element of part from mid+1 to hi are preferred to the one of the
	 * first part if, and only if, the element is strictly smaller. That way the algorithm is stable.<br />
	 * Array aux is used to copy element of array "a" before starting the merge operation and used during the merge.
	 * 
	 * @param a
	 * @param aux
	 * @param lo
	 * @param mid
	 * @param hi
	 */
	public static void merge(final Comparable<Object>[] a, final Comparable<Object>[] aux, final int lo, final int mid,
			final int hi) {
		// Copy of A into aux
		for (int i = 0; i < a.length; i++) {
			aux[i] = a[i];
		}

		int i = lo;
		int j = mid + 1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid) {
				a[k] = aux[j++];
			} else if (j > hi) {
				a[k] = aux[i++];
			} else if (MySortUtils.less(aux[j], aux[i])) {
				a[k] = aux[j++];
			} else {
				a[k] = aux[i++];
			}
		}
	}
}
