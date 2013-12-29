/**
 * 
 */
package coursera.algo.part1.impl;

import coursera.algo.part1.UnionFind;

/**
 * Quick find implementation
 */
public class QuickUnionUFImpl extends UnionFindAbstract implements UnionFind {

	private final int[] id;

	private final boolean withPathCompression;

	/**
	 * Constructor initialazing the structure of the IDs
	 * 
	 * @param N
	 */
	public QuickUnionUFImpl(final int N) {
		super(N);
		withPathCompression = false;
		if (N <= 0) {
			throw new RuntimeException("N est censé être supérieur à 0");
		}
		id = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
		}
	}

	/**
	 * Constructor initialazing the structure of the IDs
	 * 
	 * @param N
	 * @param withPathCompression
	 */
	public QuickUnionUFImpl(final int N, final boolean withPathCompression) {
		super(N);
		this.withPathCompression = withPathCompression;
		if (N <= 0) {
			throw new RuntimeException("N est censé être supérieur à 0");
		}
		id = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
		}
	}

	/**
	 * Return the root of the element i in the tree to which it belongs
	 * 
	 * @param i
	 * @return int
	 */
	private int root(int i) {
		while (i != id[i]) {
			if (withPathCompression) {
				id[i] = id[id[i]];
			}
			i = id[i];
		}
		return i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void union(final int p, final int q) {
		final int i = root(p);
		id[i] = root(q);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean connected(final int p, final int q) {
		return (root(p) == root(q));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int find(final int p) {
		return root(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count() {
		return 0;
	}

}
