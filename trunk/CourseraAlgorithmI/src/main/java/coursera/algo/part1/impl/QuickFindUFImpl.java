/**
 * 
 */
package coursera.algo.part1.impl;

import coursera.algo.part1.UnionFind;

/**
 * Quick find implementation
 */
public class QuickFindUFImpl extends UnionFindAbstract implements UnionFind {

	private final int[] id;

	public QuickFindUFImpl(final int N) {
		super(N);
		if (N <= 0) {
			throw new RuntimeException("N est censé être supérieur à 0");
		}
		id = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void union(final int p, final int q) {
		final int pid = id[p];
		final int qid = id[q];
		for (int i = 0; i < id.length; i++) {
			if (id[i] == pid) {
				id[i] = qid;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean connected(final int p, final int q) {
		return (id[p] == id[q]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int find(final int p) {
		return id[p];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count() {
		return 0;
	}

}
