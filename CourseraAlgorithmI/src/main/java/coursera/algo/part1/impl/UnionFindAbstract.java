/**
 * 
 */
package coursera.algo.part1.impl;

/**
 * Union find interface
 */
public abstract class UnionFindAbstract {

	private final int numObj;

	/**
	 * Constructeur initialisant le nombre d'éléments de l'ensemble
	 * 
	 * @param N
	 */
	public UnionFindAbstract(final int N) {
		numObj = N;
	}

	/**
	 * @return the number of elements of the set
	 */
	protected int getN() {
		return numObj;
	}

	/**
	 * Unis les deux éléments p et q
	 * 
	 * @param p
	 * @param q
	 */
	public abstract void union(int p, int q);

	/**
	 * Retourne true ou false selon que p et q sont connectés ou non
	 * 
	 * @param p
	 * @param q
	 * @return true/false
	 */
	public abstract boolean connected(int p, int q);

	/**
	 * Cherche l'élément p
	 * 
	 * @param p
	 * @return int
	 */
	public abstract int find(int p);

	/**
	 * Retourne le nombre de composants
	 * 
	 * @return int
	 */
	public abstract int count();

}
