package coursera.algo.part1.personal_implementation;

import java.util.Random;

/**
 * Class to randomly shuffle an array
 */
public class MyShuffle {

	/**
	 * Method to randomly shuffle the array of Comparable objects<br />
	 * While parsing the array from left to right (first element to last one), each time, a random number between 0 and
	 * current index step is generated and the element in that index is swapped with the current one
	 * 
	 * @param a
	 */
	public static void shuffle(final Comparable<Object>[] a) {
		if (a == null) {
			return;
		}

		final int N = a.length;
		for (int i = 1; i < N; i++) {
			final Random rand = new Random();
			final int r = rand.nextInt(i + 1);
			// Exchange value in index r with the one in index i
			if (r < i) {
				MySortUtils.exch(a, i, r);
			}
		}
	}
}
