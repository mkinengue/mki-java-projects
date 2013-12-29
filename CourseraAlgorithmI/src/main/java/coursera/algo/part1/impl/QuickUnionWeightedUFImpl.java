/**
 * 
 */
package coursera.algo.part1.impl;

import coursera.algo.part1.UnionFind;

/**
 * Quick find implementation
 */
public class QuickUnionWeightedUFImpl extends UnionFindAbstract implements UnionFind {

	private final int[] id;

	private final int[] sz;

	private final int[] depth;

	private final boolean withPathCompression;

	/**
	 * Constructor initialazing the structure of the IDs
	 * 
	 * @param N
	 */
	public QuickUnionWeightedUFImpl(final int N) {
		super(N);
		if (N <= 0) {
			throw new RuntimeException("N est censé être supérieur à 0");
		}
		withPathCompression = false;
		id = new int[N];
		sz = new int[N];
		depth = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
			sz[i] = 1;
			depth[i] = 0;
		}
	}

	/**
	 * Constructor initialazing the structure of the IDs
	 * 
	 * @param N
	 * @param withPathCompression
	 */
	public QuickUnionWeightedUFImpl(final int N, final boolean withPathCompression) {
		super(N);
		if (N <= 0) {
			throw new RuntimeException("N est censé être supérieur à 0");
		}
		this.withPathCompression = withPathCompression;
		id = new int[N];
		sz = new int[N];
		depth = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
			sz[i] = 1;
			depth[i] = 0;
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
		final int j = root(q);
		if (sz[i] < sz[j]) {
			id[i] = j;
			sz[j] += sz[i];
		} else {
			id[j] = i;
			sz[i] += sz[j];
			if (depth[i] == depth[j]) {
				depth[i] += 1;
			}
		}
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
