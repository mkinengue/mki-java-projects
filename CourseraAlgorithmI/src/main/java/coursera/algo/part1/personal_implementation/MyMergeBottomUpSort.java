package coursera.algo.part1.personal_implementation;

public class MyMergeBottomUpSort {

	private static final int CUTOFF = 7;

	@SuppressWarnings("unchecked")
	public static void sort(final Comparable<Object>[] a) {
		if ((a == null) || (a.length == 0)) {
			return;
		}

		if (a.length <= CUTOFF) {
			MyInsertionSort.sort(a);
			return;
		}

		// Create the auxiliary array
		final int N = a.length;
		final Comparable<Object>[] aux = new Comparable[N];
		for (int sz = 1; sz < N; sz = sz + sz) {
			for (int lo = 0; lo < N - sz; lo = sz + sz) {
				MySortUtils.merge(a, aux, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
			}
		}
	}
}
