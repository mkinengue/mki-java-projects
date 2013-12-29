/**
 * 
 */
package coursera.algo.part1;

/**
 * Union find interface
 */
public interface UnionFind {

	/**
	 * Unis les deux éléments p et q
	 * 
	 * @param p
	 * @param q
	 */
	public void union(int p, int q);

	/**
	 * Retourne true ou false selon que p et q sont connectés ou non
	 * 
	 * @param p
	 * @param q
	 * @return true/false
	 */
	public boolean connected(int p, int q);

	/**
	 * Cherche l'élément p
	 * 
	 * @param p
	 * @return int
	 */
	public int find(int p);

	/**
	 * Retourne le nombre de composants
	 * 
	 * @return int
	 */
	public int count();
}
