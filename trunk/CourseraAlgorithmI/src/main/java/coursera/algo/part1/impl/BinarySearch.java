package coursera.algo.part1.impl;

/**
 * @author MKINENGU Implementation of the binary search algorithm
 */
public class BinarySearch {

	/**
	 * Cherche dans le tableau "a" la clé key.<br />
	 * Retourne l'index de la clé dans le tableau si trouvé.<br />
	 * Retourne -1 si la clé n'est pas trouvée
	 * 
	 * @param a
	 * @param key
	 * @return int
	 */
	public static int binarySearch(final int[] a, final int key) {
		int lo = 0;
		int hi = a.length - 1;
		if ((key > a[hi]) || (key < a[lo])) {
			return -1;
		}
		int idx = -1;
		while (lo <= hi) {
			final int mid = (lo + hi) / 2;
			if (key < a[mid]) {
				hi = mid - 1;
			} else if (key > a[mid]) {
				lo = mid + 1;
			} else {
				idx = mid;
			}
		}
		return idx;
	}
}
